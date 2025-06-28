package zim.tave.memory.init;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.*;
import zim.tave.memory.repository.*;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DataInitializer implements CommandLineRunner {

    private final TripThemeRepository tripThemeRepository;
    private final EmotionRepository emotionRepository;
    private final WeatherRepository weatherRepository;
    private final CountryRepository countryRepository;
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
        
        // 국가 데이터 초기화
        initCountries();
        
        // 테스트 사용자 데이터 초기화
        initTestUser();
        
        log.info("기본 데이터 초기화 완료!");
    }

    private void initTripThemes() {
        if (tripThemeRepository.count() == 0) {
            log.info("여행 테마 데이터 생성 중...");
            
            TripTheme summerTheme = new TripTheme();
            summerTheme.setThemeName("여름 테마");
            tripThemeRepository.save(summerTheme);
            
            TripTheme winterTheme = new TripTheme();
            winterTheme.setThemeName("겨울 테마");
            tripThemeRepository.save(winterTheme);
            
            TripTheme springTheme = new TripTheme();
            springTheme.setThemeName("봄 테마");
            tripThemeRepository.save(springTheme);
            
            TripTheme autumnTheme = new TripTheme();
            autumnTheme.setThemeName("가을 테마");
            tripThemeRepository.save(autumnTheme);
            
            TripTheme oceanTheme = new TripTheme();
            oceanTheme.setThemeName("바다 테마");
            tripThemeRepository.save(oceanTheme);
            
            TripTheme mountainTheme = new TripTheme();
            mountainTheme.setThemeName("산 테마");
            tripThemeRepository.save(mountainTheme);
            
            log.info("여행 테마 데이터 생성 완료: {}개", tripThemeRepository.count());
        }
    }

    private void initEmotions() {
        if (emotionRepository.count() == 0) {
            log.info("감정 데이터 생성 중...");
            
            emotionRepository.save(new Emotion("행복", "#FFD700"));
            emotionRepository.save(new Emotion("설렘", "#FF69B4"));
            emotionRepository.save(new Emotion("평온", "#87CEEB"));
            emotionRepository.save(new Emotion("감동", "#FF6347"));
            emotionRepository.save(new Emotion("즐거움", "#32CD32"));
            emotionRepository.save(new Emotion("그리움", "#9370DB"));
            emotionRepository.save(new Emotion("신남", "#FFA500"));
            emotionRepository.save(new Emotion("차분함", "#708090"));
            
            log.info("감정 데이터 생성 완료: {}개", emotionRepository.count());
        }
    }

    private void initWeathers() {
        if (weatherRepository.count() == 0) {
            log.info("날씨 데이터 생성 중...");
            
            Weather sunny = new Weather();
            sunny.setName("맑음");
            sunny.setIconUrl("https://example.com/icons/sunny.png");
            weatherRepository.save(sunny);
            
            Weather cloudy = new Weather();
            cloudy.setName("흐림");
            cloudy.setIconUrl("https://example.com/icons/cloudy.png");
            weatherRepository.save(cloudy);
            
            Weather rainy = new Weather();
            rainy.setName("비");
            rainy.setIconUrl("https://example.com/icons/rainy.png");
            weatherRepository.save(rainy);
            
            Weather snowy = new Weather();
            snowy.setName("눈");
            snowy.setIconUrl("https://example.com/icons/snowy.png");
            weatherRepository.save(snowy);
            
            Weather windy = new Weather();
            windy.setName("바람");
            windy.setIconUrl("https://example.com/icons/windy.png");
            weatherRepository.save(windy);
            
            Weather foggy = new Weather();
            foggy.setName("안개");
            foggy.setIconUrl("https://example.com/icons/foggy.png");
            weatherRepository.save(foggy);
            
            log.info("날씨 데이터 생성 완료: {}개", weatherRepository.count());
        }
    }

    private void initCountries() {
        List<Country> existingCountries = countryRepository.findAll();
        if (existingCountries.isEmpty()) {
            log.info("국가 데이터 생성 중...");
            
            countryRepository.save(new Country("KR", "대한민국", "🇰🇷"));
            countryRepository.save(new Country("JP", "일본", "🇯🇵"));
            countryRepository.save(new Country("CN", "중국", "🇨🇳"));
            countryRepository.save(new Country("US", "미국", "🇺🇸"));
            countryRepository.save(new Country("GB", "영국", "🇬🇧"));
            countryRepository.save(new Country("FR", "프랑스", "🇫🇷"));
            countryRepository.save(new Country("IT", "이탈리아", "🇮🇹"));
            countryRepository.save(new Country("ES", "스페인", "🇪🇸"));
            countryRepository.save(new Country("DE", "독일", "🇩🇪"));
            countryRepository.save(new Country("TH", "태국", "🇹🇭"));
            countryRepository.save(new Country("VN", "베트남", "🇻🇳"));
            countryRepository.save(new Country("SG", "싱가포르", "🇸🇬"));
            
            log.info("국가 데이터 생성 완료: {}개", countryRepository.findAll().size());
        }
    }

    private void initTestUser() {
        // 테스트용 사용자가 없으면 생성
        if (!userRepository.findByKakaoId("test_user").isPresent()) {
            log.info("테스트 사용자 데이터 생성 중...");
            
            User testUser = new User();
            testUser.setKakaoId("test_user");
            testUser.setSurName("테스트");
            testUser.setFirstName("사용자");
            testUser.setKoreanName("테스트사용자");
            userRepository.save(testUser);
            
            log.info("테스트 사용자 데이터 생성 완료");
        }
    }
} 