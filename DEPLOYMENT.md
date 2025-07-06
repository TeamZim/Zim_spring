# 🚀 Memory 애플리케이션 배포 가이드

## 📋 사전 준비사항

### 1. 필수 파일 준비
```bash
# 프로젝트 루트 디렉토리에 다음 파일이 필요합니다:
memory-ec2-key.pem  # EC2 접속용 키 파일
```

### 2. 키 파일 권한 설정
```bash
chmod 400 memory-ec2-key.pem
```

### 3. Java 21 설치 확인
```bash
java --version
# java 21.0.x 이상이어야 함
```

### 4. SSH 연결 테스트
```bash
ssh -i memory-ec2-key.pem ec2-user@54.174.34.81 "echo 'Connection OK'"
```

## 🔧 배포 방법

### 🎯 간단한 배포 (추천)
```bash
# 프로젝트 루트 디렉토리에서 실행
./deploy-manual.sh
```

### 📊 배포 진행 단계
1. **빌드** (약 30초): Spring Boot 애플리케이션 빌드
2. **업로드** (약 10초): JAR 파일 EC2로 업로드
3. **배포** (약 20초): 기존 앱 중지 후 새 버전 시작
4. **확인** (10초 대기): 배포 상태 확인

### 🔍 배포 완료 확인
- ✅ 성공 시: "애플리케이션이 성공적으로 시작되었습니다!"
- ❌ 실패 시: 에러 로그 출력

## 🌐 배포 후 확인 URL

- **메인 서버**: http://54.174.34.81:8080
- **Swagger UI**: http://54.174.34.81:8080/swagger-ui/index.html
- **Health Check**: http://54.174.34.81:8080/actuator/health

## 🛠️ 문제 해결

### 1. 키 파일 권한 오류
```bash
# 권한 오류 발생 시
chmod 400 memory-ec2-key.pem
```

### 2. 빌드 실패
```bash
# 캐시 정리 후 다시 시도
./gradlew clean build -x test
```

### 3. SSH 연결 실패
```bash
# 키 파일 경로 확인
ls -la memory-ec2-key.pem
```

### 4. 배포 실패 시 수동 확인
```bash
# EC2 서버 로그 확인
ssh -i memory-ec2-key.pem ec2-user@54.174.34.81
tail -f ~/logs/app.log
```

## 🔒 보안 주의사항

1. **키 파일 보안**: `memory-ec2-key.pem` 파일은 절대 GitHub에 커밋하지 마세요
2. **권한 관리**: 키 파일은 반드시 `chmod 400` 권한으로 설정
3. **접근 제한**: 키 파일은 팀원에게만 공유

## 📞 도움 요청

배포 중 문제가 발생하면:
1. 에러 로그 확인
2. 위 문제 해결 방법 시도
3. 팀 채널에서 도움 요청

---

**마지막 업데이트**: 2024년 7월 6일 