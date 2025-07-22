package zim.tave.memory.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.*;
import zim.tave.memory.repository.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TripThemeRepository tripThemeRepository;
    private final EmotionRepository emotionRepository;
    private final WeatherRepository weatherRepository;
    private final UserRepository userRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        log.info("기본 데이터 초기화 시작...");
        
        // 테마 데이터 초기화
        initTripThemes();
        
        // 감정 데이터 초기화
        initEmotions();
        
        // 날씨 데이터 초기화
        initWeathers();
        
        // 테스트 사용자 데이터 초기화
        initTestUser();
        
        log.info("기본 데이터 초기화 완료!");
    }

    private void initTripThemes() {
        if (tripThemeRepository.count() == 0) {
            log.info("여행 테마 데이터 생성 중...");
            
            // 기본 테마 (ID 1)
            TripTheme basicTheme = new TripTheme("기본", 
                    "https://me-mory.mooo.com/api/files?key=images/82847c58-80f0-4488-9bad-e448bd551ebb_default(no stroke).png", 
                    "https://me-mory.mooo.com/api/files?key=images/95823625-3d74-4aa0-9661-9416c0e4a562_travel card_ripped_default.png");
            tripThemeRepository.save(basicTheme);
            
            // Grey 테마 (ID 2)
            TripTheme greyTheme = new TripTheme("Grey", 
                    "https://me-mory.mooo.com/api/files?key=images/a391584b-a811-4b6f-aab5-94493ab7a5b7_grey(no stroke).png", 
                    "https://me-mory.mooo.com/api/files?key=images/12775508-48e7-447f-a711-08286e03e733_travel card_ripped_grey.png");
            tripThemeRepository.save(greyTheme);
            
            // 탑승권 테마 (ID 3)
            TripTheme ticketTheme = new TripTheme("탑승권", 
                    "https://me-mory.mooo.com/api/files?key=images/7c59762c-2c06-40de-bcfd-e3d0bde57677_boarding-pass(no stroke).png", 
                    "https://me-mory.mooo.com/api/files?key=images/a83818f1-ecf4-4426-95f3-f512aca55195_travel card_boardingpass.png");
            tripThemeRepository.save(ticketTheme);
            
            // 액자 테마 (ID 4)
            TripTheme frameTheme = new TripTheme("액자", 
                    "https://me-mory.mooo.com/api/files?key=images/bdd320a6-da17-4d16-b39d-e57bb6c4cf68_pic-frame(no stroke).png", 
                    "https://me-mory.mooo.com/api/files?key=images/7ea4818c-8cf9-4220-8062-026b2b47929d_frame.png");
            tripThemeRepository.save(frameTheme);
            
            // Beach 테마 (ID 5)
            TripTheme beachTheme = new TripTheme("Beach", 
                    "https://me-mory.mooo.com/api/files?key=images/6c5dacb9-cead-4054-b320-184b1261b120_beach(no stroke).png", 
                    "https://me-mory.mooo.com/api/files?key=images/a96ece3e-bf03-4038-85ec-ddc58c643966_travel card_pic-frame.png");
            tripThemeRepository.save(beachTheme);
            
            // Forest 테마 (ID 6)
            TripTheme forestTheme = new TripTheme("Forest", 
                    "https://me-mory.mooo.com/api/files?key=images/02711aa3-88d7-436a-b7a4-defc51ff270b_forest(no stroke).png", 
                    "https://me-mory.mooo.com/api/files?key=images/6ee3070c-d1fb-4143-83d7-e2142bb9197b_travel card_forest.png");
            tripThemeRepository.save(forestTheme);
            
            log.info("여행 테마 데이터 생성 완료: {}개", tripThemeRepository.count());
        }
    }

    private void initEmotions() {
        if (emotionRepository.count() == 0) {
            log.info("감정 데이터 생성 중...");
            
            emotionRepository.save(new Emotion("기본", "#EEEEEE"));
            emotionRepository.save(new Emotion("설렘", "#FDD7DE"));
            emotionRepository.save(new Emotion("신기함", "#FFCB6B"));
            emotionRepository.save(new Emotion("즐거움", "#FFE13E"));
            emotionRepository.save(new Emotion("힐링", "#C1E8A0"));
            emotionRepository.save(new Emotion("평온", "#D1E5D4"));
            emotionRepository.save(new Emotion("뿌듯함", "#5ACFD5"));
            emotionRepository.save(new Emotion("해방감", "#5EB6D9"));
            emotionRepository.save(new Emotion("낯섦", "#634E72"));
            emotionRepository.save(new Emotion("긴장됨", "#2C3E50"));
            emotionRepository.save(new Emotion("외로움", "#A9A9B0"));
            emotionRepository.save(new Emotion("아쉬움", "#866868"));
            emotionRepository.save(new Emotion("벅참", "#800020"));

            
            log.info("감정 데이터 생성 완료: {}개", emotionRepository.count());
        }
    }

    private void initWeathers() {
        if (weatherRepository.count() == 0) {
            log.info("날씨 데이터 생성 중...");
            
            Weather sunny = new Weather();
            sunny.setName("맑음");
            sunny.setIconUrl("https://me-mory.mooo.com/api/files?key=images/3d2d621f-c1c6-483e-9297-a45750b05348_sunny.jpg");
            weatherRepository.save(sunny);
            
            Weather cloudy = new Weather();
            cloudy.setName("구름");
            cloudy.setIconUrl("https://me-mory.mooo.com/api/files?key=images/55d632d7-2c89-430d-b5ed-076025b450c6_cloudy.jpg");
            weatherRepository.save(cloudy);
            
            Weather rainy = new Weather();
            rainy.setName("비");
            rainy.setIconUrl("https://me-mory.mooo.com/api/files?key=images/1e9f8407-54b3-4512-af82-47555b40dc72_rainy.jpg");
            weatherRepository.save(rainy);
            
            Weather windy = new Weather();
            windy.setName("바람");
            windy.setIconUrl("https://me-mory.mooo.com/api/files?key=images/8021f201-a928-468b-be23-24fa7e07548e_windy.jpg");
            weatherRepository.save(windy);
            
            Weather snowy = new Weather();
            snowy.setName("눈");
            snowy.setIconUrl("https://me-mory.mooo.com/api/files?key=images/34e89299-35f0-4f2c-bbbf-6123e8af45f5_snowy.jpg");
            weatherRepository.save(snowy);
            
            log.info("날씨 데이터 생성 완료: {}개", weatherRepository.count());
        }
    }

    private void initTestUser() {
        // 테스트용 사용자가 없으면 생성
        if (!userRepository.findByKakaoId("test_강지혜").isPresent()) {
            log.info("테스트 사용자 데이터 생성 중...");
            
            User testUser = new User();
            testUser.setKakaoId("test_강지혜");
            testUser.setSurName("KANG");
            testUser.setFirstName("JIHYE");
            testUser.setKoreanName("강지혜");
            testUser.setBirth(java.time.LocalDate.of(1995, 3, 15));
            testUser.setNationality("REPUBLIC OF KOREA");
            testUser.setCreatedAt(java.time.LocalDate.now());
            testUser.setStatus(true);
            testUser.setProfileImageUrl("https://me-mory.mooo.com/api/files?key=images/7c24aa61-2d36-48fd-80a5-646ac518256d_IMG_3831.jpg");
            testUser.setDiaryCount(0L);
            testUser.setVisitedCountryCount(0L);
            testUser.setFlags("🇰🇷");
            userRepository.save(testUser);
            
            log.info("테스트 사용자 데이터 생성 완료");
        } else {
            log.info("테스트 사용자가 이미 존재합니다.");
        }
    }
} 