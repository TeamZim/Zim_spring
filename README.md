# [Me-mory] Backend
TAVE 15기 연합프로젝트 "Me-mory" 백엔드 소개 페이지입니다🙌🏻

## 📝 About Me-mory
### **" 여행의 순간을 감성적으로 기록하고, 나만의 취향과 분위기에 맞춰 정리할 수 있는 맞춤형 다이어리 앱.  
### &emsp; 한 편의 일기로 당신만의 이야기를 담을 수 있도록, 다양한 테마와 감정색으로 완성하는 나만의 일기 아카이브 "**

[사진]

여행의 순간을 사진, 감정, 위치, 오디오 등 다양한 정보와 함께 기록하고,
나만의 여행 타임라인과 회고를 만들어주는 감성 여행 기록 서비스입니다.
이 프로젝트의 백엔드는 Spring Boot 기반의 REST API 서버로,
다음과 같은 기능을 제공합니다:

- 여행 및 일기 관리 (사진/감정/위치 포함)
- 방문 국가 및 감정/날씨 태깅
- 카카오 로그인 기반 사용자 인증
- AWS S3 파일 업로드, 마이페이지 통계
- 타임라인 및 대표 이미지 기반 회고 지원

이 서비스는 여행의 감정과 순간을 풍부하게 남기고,
기록을 바탕으로 한 나만의 여행 히스토리를 만들어줍니다.


## 🙋🏻‍♀️ Members

<table>
  <tbody>
    <tr>
      <td align="center">
        <a href="https://github.com/lee-yeonseo">
          <img src="https://avatars.githubusercontent.com/lee-yeonseo" width="100px;" alt="yeonseo lee"/>
          <br /><sub><b>이연서</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/zzmnxn">
          <img src="https://avatars.githubusercontent.com/zzmnxn" width="100px;" alt="박지민"/>
          <br /><sub><b>박지민</b></sub>
        </a>
      </td>
      <td align="center">
        <a href="https://github.com/chwwwon">
          <img src="https://avatars.githubusercontent.com/chwwwon" width="100px;" alt="최형원"/>
          <br /><sub><b>최형원</b></sub>
        </a>
      </td>
    </tr>
  </tbody>
</table>

## 📄 Dependency

| Dependency Tool | Version |
|------------------|---------|
| Gradle           | 8.7     |
| Java             | 21      |
| Spring Boot      | 3.5.0   |
| MySQL            | 8.0.x   |
| Swagger (springdoc-openapi) | 2.1.0   |
| AWS SDK (S3)     | 2.20.89 |



## 🛠️ Tech Stack

| Category       | Stack                                                     |
|----------------|-----------------------------------------------------------|
| Framework      | Spring Boot                                               |
| ORM            | Spring Data JPA                                           |
| Authorization  | Kakao OAuth2.0 Login                                      |
| Database       | AWS RDS (MySQL 8.0)                                       |
| File Storage   | AWS S3                                                    |
| CI/CD          | GitHub Actions + Docker Hub                               |
| Deployment     | AWS EC2 (Docker Container)                                |
| API Doc        | Swagger UI                                                |

---

## 🛠️ Architecture
<img width="1175" height="945" alt="Me-mory_diagram drawio" src="https://github.com/user-attachments/assets/4295f821-ba83-4e27-8cd3-855ea03d1f7a" />


## ☁️ ERD
![DCEF280F-AD70-4C20-A027-DD106DE94855_1_201_a](https://github.com/user-attachments/assets/c0c48458-7093-4fc9-87fa-7062ffeb4f74)


## 💻 API
#### 로그인/회원가입
| Method | Endpoint           | 설명       |
| ------ | ------------------ | -------- |
| POST   | `/api/login/kakao` | 카카오 로그인  |
| POST   | `/api/join`        | 회원 정보 입력 |

#### 여행
| Method | Endpoint                   | 설명          |
| ------ | -------------------------- | ----------- |
| POST   | `/api/trips`               | 여행 생성       |
| GET    | `/api/trips`               | 전체 여행 목록 조회 |
| GET    | `/api/trips/{tripId}`      | 특정 여행 상세 조회 |
| GET    | `/api/trips/user/{userId}` | 사용자별 여행 목록  |

#### 일기
| Method | Endpoint                     | 설명            |
| ------ | ---------------------------- | ------------- |
| POST   | `/api/diaries`               | 일기 생성         |
| GET    | `/api/diaries`               | 전체 일기 목록 조회   |
| GET    | `/api/diaries/{diaryId}`     | 특정 일기 상세 조회   |
| GET    | `/api/diaries/user/{userId}` | 사용자별 일기 목록 조회 |

#### 방문 국가 / 감정 / 날씨 / 테마
| Method | Endpoint                           | 설명            |
| ------ | ---------------------------------- | ------------- |
| GET    | `/api/countries/{userId}`          | 사용자별 방문 국가 목록 |
| GET    | `/api/countries/search?keyword=한국` | 국가명 검색        |
| GET    | `/api/emotions`                    | 감정 목록 조회      |
| GET    | `/api/weathers`                    | 날씨 목록 조회      |
| GET    | `/api/themes`                      | 여행 테마 목록 조회   |

#### 파일 업로드 / 다운로드
| Method | Endpoint             | 설명      |
| ------ | -------------------- | ------- |
| POST   | `/upload`            | 파일 업로드  |
| GET    | `/api/files?key=...` | 파일 다운로드 |

 -Swagger UI: [🔗 바로가기](https://me-mory.mooo.com/swagger-ui/index.html#/)
