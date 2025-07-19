#!/bin/bash

# Memory 애플리케이션 운영 서버 빠른 Docker 배포 스크립트

# 배포 설정
EC2_HOST="54.174.34.81"
EC2_USER="ec2-user"
EC2_KEY="${EC2_KEY:-memory-ec2-key.pem}"

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m'

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

# 메인 실행
main() {
    log "🚀 빠른 운영 서버 Docker 배포 시작"
    
    # 1. JAR 파일 및 Docker 관련 파일 전송
    log "1. 필요한 파일들 전송 중..."
    scp -i "$EC2_KEY" build/libs/memory-0.0.1-SNAPSHOT.jar "$EC2_USER@$EC2_HOST":~/
    scp -i "$EC2_KEY" Dockerfile "$EC2_USER@$EC2_HOST":~/
    scp -i "$EC2_KEY" nginx.conf "$EC2_USER@$EC2_HOST":~/
    
    # 2. EC2에서 직접 Docker 작업 실행
    log "2. EC2에서 Docker 배포 실행 중..."
    ssh -i "$EC2_KEY" "$EC2_USER@$EC2_HOST" << 'EOF'
        # 기존 Docker 컨테이너 정리
        echo "기존 컨테이너 중지 및 제거..."
        docker stop memory-app memory-nginx 2>/dev/null || true
        docker rm memory-app memory-nginx 2>/dev/null || true
        docker rmi memory-app 2>/dev/null || true
        
        # 간단한 Dockerfile 생성 (이미 전송된 JAR 사용)
        cat > Dockerfile << 'DOCKER_EOF'
FROM openjdk:21-jdk-slim
WORKDIR /app
COPY memory-0.0.1-SNAPSHOT.jar app.jar
EXPOSE 8081
ENTRYPOINT ["java", "-jar", "app.jar"]
DOCKER_EOF
        
        # Docker 이미지 빌드 (빠르게)
        echo "Docker 이미지 빌드 중..."
        docker build -t memory-app .
        
        # 환경 변수 로드
        source ~/.env
        
        # Spring Boot 애플리케이션 컨테이너 실행
        echo "Spring Boot 애플리케이션 컨테이너 실행 중..."
        docker run -d \
          --name memory-app \
          --restart unless-stopped \
          -p 8081:8081 \
          -e SPRING_DATASOURCE_URL="jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME}?useUnicode=true&characterEncoding=utf8&useSSL=false&serverTimezone=Asia/Seoul&allowPublicKeyRetrieval=true" \
          -e SPRING_DATASOURCE_USERNAME="${DB_USERNAME}" \
          -e SPRING_DATASOURCE_PASSWORD="${DB_PASSWORD}" \
          -e CLOUD_AWS_CREDENTIALS_ACCESS_KEY="${AWS_ACCESS_KEY_ID}" \
          -e CLOUD_AWS_CREDENTIALS_SECRET_KEY="${AWS_SECRET_ACCESS_KEY}" \
          -e CLOUD_AWS_REGION_STATIC="ap-northeast-2" \
          -e CLOUD_AWS_S3_BUCKET="${S3_BUCKET_NAME}" \
          memory-app
        
        # nginx 컨테이너 실행
        echo "nginx 컨테이너 실행 중..."
        docker run -d \
          --name memory-nginx \
          --restart unless-stopped \
          -p 80:80 \
          --link memory-app:app \
          -v ~/nginx.conf:/etc/nginx/nginx.conf \
          nginx:alpine
        
        echo "배포 완료!"
        
        # 상태 확인
        echo "=== 컨테이너 상태 ==="
        docker ps | grep memory
        
        # 헬스체크 (짧은 시간 대기)
        echo "=== 애플리케이션 시작 대기 (30초) ==="
        sleep 30
        
        if curl -f http://localhost/health 2>/dev/null; then
            echo "✅ 애플리케이션 정상 작동!"
        else
            echo "⏳ 애플리케이션 시작 중... (조금 더 기다려주세요)"
            echo "최근 로그:"
            docker logs memory-app --tail 10
        fi
EOF
    
    success "🎉 빠른 Docker 배포 완료!"
    log "접속 URL: https://me-mory.mooo.com"
    log "API 테스트: https://me-mory.mooo.com/api/trips"
    
    # 최종 확인
    log "3. 최종 상태 확인 중..."
    sleep 10
    if curl -f https://me-mory.mooo.com/health 2>/dev/null; then
        success "✅ 운영 서버 정상 작동 확인!"
    else
        warning "⏳ 아직 시작 중일 수 있습니다. 잠시 후 다시 확인해주세요."
    fi
}

# 스크립트 실행
main "$@" 