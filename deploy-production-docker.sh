#!/bin/bash

# Memory 애플리케이션 운영 서버 Docker 배포 스크립트

# 배포 설정
EC2_HOST="54.174.34.81"
EC2_USER="ec2-user"
EC2_KEY="${EC2_KEY:-memory-ec2-key.pem}"
IMAGE_NAME="memory-app"
TAG="latest"

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

# 사전 검사
check_prerequisites() {
    log "사전 검사 시작..."
    
    # 키 파일 존재 확인
    if [ ! -f "$EC2_KEY" ]; then
        error "키 파일을 찾을 수 없습니다: $EC2_KEY"
        exit 1
    fi
    
    # Docker 이미지 존재 확인
    if ! docker images | grep -q "$IMAGE_NAME"; then
        error "Docker 이미지를 찾을 수 없습니다: $IMAGE_NAME"
        log "먼저 'docker build -t $IMAGE_NAME .' 를 실행하세요"
        exit 1
    fi
    
    # SSH 연결 테스트
    log "EC2 연결 테스트 중..."
    if ! ssh -i "$EC2_KEY" -o ConnectTimeout=5 -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" "echo 'Connection OK'" >/dev/null 2>&1; then
        error "EC2 연결 실패"
        exit 1
    fi
    
    success "사전 검사 완료"
}

# 기존 JAR 프로세스 중지
stop_existing_service() {
    log "기존 JAR 프로세스 중지 중..."
    
    ssh -i "$EC2_KEY" "$EC2_USER@$EC2_HOST" << 'EOF'
        # Java 프로세스 찾기 및 중지
        JAVA_PID=$(ps aux | grep "memory-0.0.1-SNAPSHOT.jar" | grep -v grep | awk '{print $2}')
        if [ ! -z "$JAVA_PID" ]; then
            echo "기존 Java 프로세스 중지: PID $JAVA_PID"
            kill -15 $JAVA_PID
            sleep 5
            # 강제 종료가 필요한 경우
            if ps -p $JAVA_PID > /dev/null; then
                echo "강제 종료 실행"
                kill -9 $JAVA_PID
            fi
        else
            echo "실행 중인 Java 프로세스를 찾을 수 없습니다"
        fi
        
        # 기존 Docker 컨테이너 중지 및 제거
        docker stop memory-app memory-nginx 2>/dev/null || true
        docker rm memory-app memory-nginx 2>/dev/null || true
EOF
    
    success "기존 서비스 중지 완료"
}

# Docker 이미지 전송
transfer_docker_image() {
    log "Docker 이미지 전송 중..."
    
    # 이미지를 tar 파일로 저장
    docker save $IMAGE_NAME:$TAG | gzip > memory-app.tar.gz
    
    # EC2로 이미지 전송
    scp -i "$EC2_KEY" memory-app.tar.gz "$EC2_USER@$EC2_HOST":~/
    
    # nginx 설정 파일 전송
    scp -i "$EC2_KEY" nginx.conf "$EC2_USER@$EC2_HOST":~/
    
    # 로컬 tar 파일 삭제
    rm memory-app.tar.gz
    
    success "Docker 이미지 전송 완료"
}

# 운영 서버에 Docker 배포
deploy_docker() {
    log "운영 서버에 Docker 배포 시작..."
    
    ssh -i "$EC2_KEY" "$EC2_USER@$EC2_HOST" << 'EOF'
        # Docker 이미지 로드
        echo "Docker 이미지 로드 중..."
        docker load < memory-app.tar.gz
        
        # 기존 환경 변수 로드
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
          -e CLOUD_AWS_REGION_STATIC="${AWS_REGION}" \
          -e CLOUD_AWS_S3_BUCKET="${S3_BUCKET_NAME}" \
          memory-app:latest
        
        # nginx 컨테이너 실행
        echo "nginx 컨테이너 실행 중..."
        docker run -d \
          --name memory-nginx \
          --restart unless-stopped \
          -p 80:80 \
          --link memory-app:app \
          -v ~/nginx.conf:/etc/nginx/nginx.conf \
          nginx:alpine
        
        # 정리
        rm memory-app.tar.gz
        
        echo "배포 완료!"
EOF
    
    success "Docker 배포 완료"
}

# 서비스 상태 확인
check_service_status() {
    log "서비스 상태 확인 중..."
    
    ssh -i "$EC2_KEY" "$EC2_USER@$EC2_HOST" << 'EOF'
        echo "=== Docker 컨테이너 상태 ==="
        docker ps | grep memory
        
        echo ""
        echo "=== 애플리케이션 헬스체크 (60초 대기) ==="
        sleep 60
        
        if curl -f http://localhost/health 2>/dev/null; then
            echo "✅ 애플리케이션 정상 작동"
        else
            echo "❌ 애플리케이션 상태 확인 실패"
            echo "애플리케이션 로그:"
            docker logs memory-app --tail 20
        fi
EOF
    
    success "서비스 상태 확인 완료"
}

# 메인 실행
main() {
    log "운영 서버 Docker 배포 시작"
    
    check_prerequisites
    stop_existing_service
    transfer_docker_image
    deploy_docker
    check_service_status
    
    success "🎉 운영 서버 Docker 배포 완료!"
    log "접속 URL: https://me-mory.mooo.com"
    log "API 테스트: https://me-mory.mooo.com/api/trips"
    
    warning "⚠️ 환경 변수 설정을 확인하세요!"
    log "실제 데이터베이스 및 AWS 정보로 스크립트를 수정해야 합니다."
}

# 스크립트 실행
main "$@" 