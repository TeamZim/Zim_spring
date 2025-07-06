#!/bin/bash

# Memory 애플리케이션 수동 배포 스크립트
# 로컬에서 실행하는 스크립트

EC2_HOST="54.174.34.81"
EC2_KEY="memory-ec2-key.pem"
EC2_USER="ec2-user"

echo "🚀 Memory 애플리케이션 수동 배포 시작"
echo "=========================================="

# 1. JAR 파일 빌드
echo "1. 애플리케이션 빌드 중..."
./gradlew build -x test

# 2. JAR 파일 EC2로 업로드
echo "2. JAR 파일 업로드 중..."
scp -i "$EC2_KEY" -o StrictHostKeyChecking=no build/libs/memory-0.0.1-SNAPSHOT.jar "$EC2_USER@$EC2_HOST:/home/ec2-user/app/"

# 3. 애플리케이션 배포 및 시작
echo "3. 애플리케이션 배포 및 시작 중..."
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

echo "4. 배포 완료 확인 (10초 대기)..."
sleep 10

# 5. 배포 확인
ssh -i "$EC2_KEY" -o StrictHostKeyChecking=no "$EC2_USER@$EC2_HOST" << 'EOF'
if pgrep -f "memory-0.0.1-SNAPSHOT.jar" > /dev/null; then
  echo "✅ 애플리케이션이 성공적으로 시작되었습니다!"
  echo "최근 로그:"
  tail -10 ~/logs/app.log
else
  echo "❌ 애플리케이션 시작에 실패했습니다."
  echo "에러 로그:"
  cat ~/logs/app.log
fi
EOF

echo "=========================================="
echo "🎉 수동 배포 완료!"
echo "📍 애플리케이션 URL: http://$EC2_HOST:8080"
echo "📊 Swagger UI: http://$EC2_HOST:8080/swagger-ui/index.html" 