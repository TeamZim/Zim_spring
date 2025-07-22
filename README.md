# Zim_spring Backend
TAVE 15기 연합프로젝트 "Me-mory" 소개 페이지입니다🙌🏻

## About "Me-mory"

## Members

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

## Dependency

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
