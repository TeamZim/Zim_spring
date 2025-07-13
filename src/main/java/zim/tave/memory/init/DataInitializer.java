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
                    "https://images.example.com/themes/basic/sample.jpg", 
                    "https://images.example.com/themes/basic/card.jpg");
            tripThemeRepository.save(basicTheme);
            
            // Grey 테마 (ID 2)
            TripTheme greyTheme = new TripTheme("Grey", 
                    "https://images.example.com/themes/grey/sample.jpg", 
                    "https://images.example.com/themes/grey/card.jpg");
            tripThemeRepository.save(greyTheme);
            
            // 탑승권 테마 (ID 3)
            TripTheme ticketTheme = new TripTheme("탑승권", 
                    "https://images.example.com/themes/ticket/sample.jpg", 
                    "https://images.example.com/themes/ticket/card.jpg");
            tripThemeRepository.save(ticketTheme);
            
            // 액자 테마 (ID 4)
            TripTheme frameTheme = new TripTheme("액자", 
                    "https://images.example.com/themes/frame/sample.jpg", 
                    "https://images.example.com/themes/frame/card.jpg");
            tripThemeRepository.save(frameTheme);
            
            // Beach 테마 (ID 5)
            TripTheme beachTheme = new TripTheme("Beach", 
                    "https://images.example.com/themes/beach/sample.jpg", 
                    "https://images.example.com/themes/beach/card.jpg");
            tripThemeRepository.save(beachTheme);
            
            // Forest 테마 (ID 6)
            TripTheme forestTheme = new TripTheme("Forest", 
                    "https://images.example.com/themes/forest/sample.jpg", 
                    "https://images.example.com/themes/forest/card.jpg");
            tripThemeRepository.save(forestTheme);
            
            log.info("여행 테마 데이터 생성 완료: {}개", tripThemeRepository.count());
        }
    }

    private void initEmotions() {
        if (emotionRepository.count() == 0) {
            log.info("감정 데이터 생성 중...");
            
            emotionRepository.save(new Emotion("기본", "#D9D9D9"));
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
            sunny.setIconUrl("/images/weather/sunny.png");
            weatherRepository.save(sunny);
            
            Weather cloudy = new Weather();
            cloudy.setName("구름");
            cloudy.setIconUrl("/images/weather/cloudy.png");
            weatherRepository.save(cloudy);
            
            Weather rainy = new Weather();
            rainy.setName("비");
            rainy.setIconUrl("/images/weather/rainy.png");
            weatherRepository.save(rainy);
            
            Weather windy = new Weather();
            windy.setName("바람");
            windy.setIconUrl("/images/weather/windy.png");
            weatherRepository.save(windy);
            
            Weather snowy = new Weather();
            snowy.setName("눈");
            snowy.setIconUrl("/images/weather/snowy.png");
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
            testUser.setSurName("강");
            testUser.setFirstName("지혜");
            testUser.setKoreanName("강지혜");
            userRepository.save(testUser);
            
            log.info("테스트 사용자 데이터 생성 완료");
        } else {
            log.info("테스트 사용자가 이미 존재합니다.");
        }
    }
} 