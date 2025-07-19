#!/bin/bash

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# 로그 함수
log() {
    echo -e "${BLUE}[$(date '+%Y-%m-%d %H:%M:%S')] $1${NC}"
}

error() {
    echo -e "${RED}[ERROR] $1${NC}"
}

success() {
    echo -e "${GREEN}[SUCCESS] $1${NC}"
}

warning() {
    echo -e "${YELLOW}[WARNING] $1${NC}"
}

# .env 파일 확인
if [ ! -f .env ]; then
    error ".env 파일이 존재하지 않습니다."
    warning "다음 내용으로 .env 파일을 생성하세요:"
    echo "DB_HOST=your_db_host"
    echo "DB_PORT=3306"
    echo "DB_NAME=memory_db"
    echo "DB_USERNAME=your_username"
    echo "DB_PASSWORD=your_password"
    echo "AWS_ACCESS_KEY_ID=your_aws_access_key"
    echo "AWS_SECRET_ACCESS_KEY=your_aws_secret_key"
    echo "AWS_REGION=ap-northeast-2"
    echo "S3_BUCKET_NAME=your_bucket_name"
    exit 1
fi

# .env 파일 로드
source .env

log "Docker 배포 시작..."

# 1. 프로젝트 빌드
log "1. 프로젝트 빌드 중..."
./gradlew clean build -x test
if [ $? -ne 0 ]; then
    error "빌드 실패"
    exit 1
fi
success "빌드 완료"

# 2. 기존 컨테이너 중지 및 제거
log "2. 기존 컨테이너 중지 및 제거..."
docker-compose down --remove-orphans
docker system prune -f

# 3. Docker 이미지 빌드
log "3. Docker 이미지 빌드 중..."
docker build -t memory-app:latest .
if [ $? -ne 0 ]; then
    error "Docker 이미지 빌드 실패"
    exit 1
fi
success "Docker 이미지 빌드 완료"

# 4. Docker 컨테이너 실행
log "4. Docker 컨테이너 실행 중..."
docker run -d \
  --name memory-app \
  --restart unless-stopped \
  -p 8081:8081 \
  -e SPRING_DATASOURCE_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true" \
  -e SPRING_DATASOURCE_USERNAME="${DB_USERNAME}" \
  -e SPRING_DATASOURCE_PASSWORD="${DB_PASSWORD}" \
  -e CLOUD_AWS_CREDENTIALS_ACCESS_KEY="${AWS_ACCESS_KEY_ID}" \
  -e CLOUD_AWS_CREDENTIALS_SECRET_KEY="${AWS_SECRET_ACCESS_KEY}" \
  -e CLOUD_AWS_REGION_STATIC="${AWS_REGION}" \
  -e CLOUD_AWS_S3_BUCKET="${S3_BUCKET_NAME}" \
  memory-app:latest

if [ $? -ne 0 ]; then
    error "컨테이너 실행 실패"
    exit 1
fi

# 5. nginx 컨테이너 실행
log "5. nginx 컨테이너 실행 중..."
docker run -d \
  --name memory-nginx \
  --restart unless-stopped \
  -p 80:80 \
  --link memory-app:app \
  -v $(pwd)/nginx.conf:/etc/nginx/nginx.conf \
  nginx:alpine

if [ $? -ne 0 ]; then
    error "nginx 컨테이너 실행 실패"
    exit 1
fi

# 6. 컨테이너 상태 확인
log "6. 컨테이너 상태 확인 중..."
sleep 10

if [ "$(docker ps -q -f name=memory-app)" ]; then
    success "memory-app 컨테이너 정상 실행 중"
else
    error "memory-app 컨테이너 실행 실패"
    log "컨테이너 로그:"
    docker logs memory-app
    exit 1
fi

if [ "$(docker ps -q -f name=memory-nginx)" ]; then
    success "memory-nginx 컨테이너 정상 실행 중"
else
    error "memory-nginx 컨테이너 실행 실패"
    log "컨테이너 로그:"
    docker logs memory-nginx
    exit 1
fi

# 7. 애플리케이션 시작 대기
log "7. 애플리케이션 시작 대기 중..."
for i in {1..60}; do
    if curl -s http://localhost:8081/actuator/health > /dev/null 2>&1; then
        success "애플리케이션 정상 시작 완료"
        break
    fi
    
    if [ $i -eq 60 ]; then
        error "애플리케이션 시작 시간 초과"
        log "애플리케이션 로그:"
        docker logs memory-app --tail 50
        exit 1
    fi
    
    log "애플리케이션 시작 대기 중... (${i}/60)"
    sleep 5
done

# 8. 최종 상태 확인
log "8. 최종 상태 확인..."
docker ps | grep memory

success "Docker 배포 완료!"
log "접속 URL: http://localhost"
log "API 테스트: http://localhost/weathers"
log "헬스체크: http://localhost/health"

log "컨테이너 로그 확인:"
echo "  - 애플리케이션 로그: docker logs memory-app"
echo "  - nginx 로그: docker logs memory-nginx"

log "컨테이너 중지:"
echo "  - docker stop memory-app memory-nginx"
echo "  - docker rm memory-app memory-nginx" 