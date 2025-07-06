#!/bin/bash

# Memory 애플리케이션 수동 배포 스크립트
# 로컬에서 실행하는 스크립트

# 배포 설정
EC2_HOST="54.174.34.81"
EC2_USER="ec2-user"
EC2_KEY="${EC2_KEY:-memory-ec2-key.pem}"  # 환경변수 또는 기본값 사용
JAR_FILE="build/libs/memory-0.0.1-SNAPSHOT.jar"

# 색상 정의
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 함수 정의
print_info() {
    echo -e "${GREEN}[INFO]${NC} $1"
}

print_warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1"
}

print_error() {
    echo -e "${RED}[ERROR]${NC} $1"
}

# 사전 검사
check_prerequisites() {
    # 키 파일 존재 확인
    if [ ! -f "$EC2_KEY" ]; then
        print_error "키 파일을 찾을 수 없습니다: $EC2_KEY"
        print_info "키 파일을 프로젝트 루트 디렉토리에 복사하세요:"
        echo "  cp your-key.pem $EC2_KEY"
        echo "  chmod 400 $EC2_KEY"
        exit 1
    fi
    
    # 키 파일 권한 확인
    if [ "$(stat -c %a "$EC2_KEY" 2>/dev/null || stat -f %A "$EC2_KEY" 2>/dev/null)" != "400" ]; then
        print_warning "키 파일 권한을 수정합니다..."
        chmod 400 "$EC2_KEY"
    fi
    
    # SSH 연결 테스트
    print_info "EC2 연결 테스트 중..."
    if ! ssh -i "$EC2_KEY" -o ConnectTimeout=5 -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" "echo 'Connection OK'" >/dev/null 2>&1; then
        print_error "EC2 서버에 연결할 수 없습니다. 네트워크 및 키 파일을 확인하세요."
        exit 1
    fi
    
    print_info "✅ 사전 검사 완료"
}

print_info "🚀 Memory 애플리케이션 수동 배포 시작"
print_info "EC2 서버: $EC2_HOST"
print_info "키 파일: $EC2_KEY"
echo "=========================================="

# 사전 검사
check_prerequisites

# 1. JAR 파일 빌드
print_info "1. 애플리케이션 빌드 중..."
if ! ./gradlew build -x test; then
    print_error "빌드 실패"
    exit 1
fi

# JAR 파일 존재 확인
if [ ! -f "$JAR_FILE" ]; then
    print_error "JAR 파일을 찾을 수 없습니다: $JAR_FILE"
    exit 1
fi

# 2. JAR 파일 EC2로 업로드
print_info "2. JAR 파일 업로드 중..."
if ! scp -i "$EC2_KEY" -o StrictHostKeyChecking=no "$JAR_FILE" "$EC2_USER@$EC2_HOST:/home/ec2-user/app/"; then
    print_error "파일 업로드 실패"
    exit 1
fi

# 3. 애플리케이션 배포 및 시작
print_info "3. 애플리케이션 배포 및 시작 중..."
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
# 기존 애플리케이션 중지
sudo pkill -f "memory-0.0.1-SNAPSHOT.jar" || true

# 환경변수 로드
source ~/.env

# 앱 디렉토리로 이동
cd ~/app

# 애플리케이션 시작
nohup java -Xmx350m -Xms256m -XX:+UseG1GC \
  -Dspring.datasource.url=jdbc:mysql://${DB_HOST}:${DB_PORT}/${DB_NAME} \
  -Dspring.datasource.username=${DB_USERNAME} \
  -Dspring.datasource.password=${DB_PASSWORD} \
  -Dcloud.aws.credentials.access-key=${AWS_ACCESS_KEY_ID} \
  -Dcloud.aws.credentials.secret-key=${AWS_SECRET_ACCESS_KEY} \
  -Dcloud.aws.region.static=us-east-1 \
  -Dcloud.aws.s3.bucket=${S3_BUCKET_NAME} \
  -jar memory-0.0.1-SNAPSHOT.jar --spring.profiles.active=prod > ~/logs/app.log 2>&1 &

echo "애플리케이션 시작 완료"
EOF

print_info "4. 배포 완료 확인 (10초 대기)..."
sleep 10

# 5. 배포 확인
print_info "5. 배포 상태 확인 중..."
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
if pgrep -f "memory-0.0.1-SNAPSHOT.jar" > /dev/null; then
  echo -e "\033[0;32m✅ 애플리케이션이 성공적으로 시작되었습니다!\033[0m"
  echo "최근 로그:"
  tail -10 ~/logs/app.log
else
  echo -e "\033[0;31m❌ 애플리케이션 시작에 실패했습니다.\033[0m"
  echo "에러 로그:"
  cat ~/logs/app.log
fi
EOF

echo "=========================================="
print_info "🎉 수동 배포 완료!"
echo "📍 애플리케이션 URL: http://$EC2_HOST:8080"
echo "📊 Swagger UI: http://$EC2_HOST:8080/swagger-ui/index.html"
echo "🔍 Health Check: http://$EC2_HOST:8080/actuator/health" 