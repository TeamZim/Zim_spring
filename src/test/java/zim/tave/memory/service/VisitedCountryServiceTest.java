package zim.tave.memory.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Country;
import zim.tave.memory.domain.Emotion;
import zim.tave.memory.domain.User;
import zim.tave.memory.domain.VisitedCountry;
import zim.tave.memory.repository.*;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class VisitedCountryServiceTest {

    @Autowired
    private VisitedCountryService visitedCountryService;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private CountryRepository countryRepository;
    @Autowired
    private EmotionRepository emotionRepository;
    @Autowired
    private VisitedCountryRepository visitedCountryRepository;

    private Long userId;
    private String countryCode = "KR";
    private Long emotionId;

    @BeforeEach
    void setUp() {
        // 테스트용 유저, 국가, 감정 등록
        User user = new User();
        user.setKakaoId("testKakao");
        user.setFirstName("Hong");
        user.setSurName("Gil");
        userRepository.save(user);
        userId = user.getId();

        Country country = new Country();
        country.setCountryCode("KR");
        country.setCountryName("대한민국");
        country.setEmoji("🇰🇷");
        countryRepository.save(country);

        Emotion emotion = new Emotion();
        emotion.setName("행복");
        emotion.setColorCode("#FFD700");
        emotionRepository.save(emotion);
        emotionId = emotion.getId();
    }

    @Test
    void 방문국가_정상등록_및_중복방지() {
        // 등록
        visitedCountryService.registerVisitedCountry(userId, countryCode, emotionId);

        // 중복 방지
        visitedCountryService.registerVisitedCountry(userId, countryCode, emotionId); // 다시 시도해도 저장 X

        List<VisitedCountry> visitedList = visitedCountryService.getVisitedCountries(userId);

        assertThat(visitedList).hasSize(1); // 한 번만 저장됨
        assertThat(visitedList.get(0).getCountry().getCountryCode()).isEqualTo("KR");
    }

    @Test
    void 방문여부_확인() {
        boolean before = visitedCountryService.alreadyVisited(userId, countryCode);
        assertThat(before).isFalse();

        visitedCountryService.registerVisitedCountry(userId, countryCode, emotionId);

        boolean after = visitedCountryService.alreadyVisited(userId, countryCode);
        assertThat(after).isTrue();
    }
}
