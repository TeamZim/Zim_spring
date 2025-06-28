package zim.tave.memory.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Country;
import zim.tave.memory.domain.Emotion;
import zim.tave.memory.domain.User;
import zim.tave.memory.domain.VisitedCountry;
import zim.tave.memory.repository.*;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

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

    @Test
    void testRegisterVisitedCountry_SuccessAndDuplicatePrevention() {
        // given: 테스트용 데이터 생성
        User user = createTestUser("testKakao1");
        Country country = createTestCountry("KR1", "대한민국1", "🇰🇷");
        Emotion emotion = createTestEmotion("행복1", "#FFD700");
        
        // when: 등록
        visitedCountryService.registerVisitedCountry(user.getId(), country.getCountryCode(), emotion.getId());

        // 중복 방지
        visitedCountryService.registerVisitedCountry(user.getId(), country.getCountryCode(), emotion.getId()); // 다시 시도해도 저장 X

        List<VisitedCountry> visitedList = visitedCountryService.getVisitedCountries(user.getId());

        // then: 한 번만 저장됨
        assertThat(visitedList).hasSize(1);
        assertThat(visitedList.get(0).getCountry().getCountryCode()).isEqualTo("KR1");
    }

    @Test
    void testAlreadyVisited() {
        // given: 테스트용 데이터 생성
        User user = createTestUser("testKakao2");
        Country country = createTestCountry("KR2", "대한민국2", "🇰🇷");
        Emotion emotion = createTestEmotion("행복2", "#FFD700");
        
        // when & then: 방문 여부 확인
        boolean before = visitedCountryService.alreadyVisited(user.getId(), country.getCountryCode());
        assertThat(before).isFalse();

        visitedCountryService.registerVisitedCountry(user.getId(), country.getCountryCode(), emotion.getId());

        boolean after = visitedCountryService.alreadyVisited(user.getId(), country.getCountryCode());
        assertThat(after).isTrue();
    }

    @Test
    void testUpdateVisitedCountryColor_Success() {
        // given: 테스트용 데이터 생성
        User user = createTestUser("testKakao3");
        Country country = createTestCountry("KR3", "대한민국3", "🇰🇷");
        Emotion emotion = createTestEmotion("행복3", "#FFD700");
        
        // 방문 국가 등록
        visitedCountryService.registerVisitedCountry(user.getId(), country.getCountryCode(), emotion.getId());
        
        // when: 색상 업데이트
        String newColor = "#FF0000";
        visitedCountryService.updateVisitedCountryColor(user.getId(), country.getCountryCode(), newColor);
        
        // then: 업데이트된 색상 확인
        List<VisitedCountry> visitedList = visitedCountryService.getVisitedCountries(user.getId());
        assertThat(visitedList).hasSize(1);
        assertThat(visitedList.get(0).getColor()).isEqualTo(newColor);
    }

    @Test
    void testUpdateVisitedCountryColor_NotFound() {
        // given: 테스트용 데이터 생성
        User user = createTestUser("testKakao4");
        Country country = createTestCountry("KR4", "대한민국4", "🇰🇷");
        
        // when & then: 존재하지 않는 기록에 대해 색상 업데이트 시도 시 예외 발생
        String newColor = "#FF0000";
        assertThatThrownBy(() -> 
            visitedCountryService.updateVisitedCountryColor(user.getId(), country.getCountryCode(), newColor)
        ).isInstanceOf(IllegalArgumentException.class)
         .hasMessage("해당 국가 기록이 존재하지 않습니다.");
    }

    @Test
    void testUpdateVisitedCountryColor_MultipleUpdates() {
        // given: 테스트용 데이터 생성
        User user = createTestUser("testKakao5");
        Country country = createTestCountry("KR5", "대한민국5", "🇰🇷");
        Emotion emotion = createTestEmotion("행복5", "#FFD700");
        
        // 방문 국가 등록
        visitedCountryService.registerVisitedCountry(user.getId(), country.getCountryCode(), emotion.getId());
        
        // when: 색상을 여러 번 업데이트
        String color1 = "#FF0000";
        String color2 = "#00FF00";
        String color3 = "#0000FF";
        
        visitedCountryService.updateVisitedCountryColor(user.getId(), country.getCountryCode(), color1);
        visitedCountryService.updateVisitedCountryColor(user.getId(), country.getCountryCode(), color2);
        visitedCountryService.updateVisitedCountryColor(user.getId(), country.getCountryCode(), color3);
        
        // then: 마지막 색상으로 업데이트됨
        List<VisitedCountry> visitedList = visitedCountryService.getVisitedCountries(user.getId());
        assertThat(visitedList).hasSize(1);
        assertThat(visitedList.get(0).getColor()).isEqualTo(color3);
    }

    @Test
    void testUpdateVisitedCountryColor_OtherUserUnaffected() {
        // given: 두 사용자와 방문 국가 등록
        User user1 = createTestUser("testKakao6");
        User user2 = createTestUser("testKakao7");
        Country country = createTestCountry("KR6", "대한민국6", "🇰🇷");
        Emotion emotion = createTestEmotion("행복6", "#FFD700");
        
        visitedCountryService.registerVisitedCountry(user1.getId(), country.getCountryCode(), emotion.getId());
        visitedCountryService.registerVisitedCountry(user2.getId(), country.getCountryCode(), emotion.getId());
        
        // when: 한 사용자의 색상만 업데이트
        String newColor = "#FF0000";
        visitedCountryService.updateVisitedCountryColor(user1.getId(), country.getCountryCode(), newColor);
        
        // then: 다른 사용자의 색상은 변경되지 않음
        List<VisitedCountry> user1List = visitedCountryService.getVisitedCountries(user1.getId());
        List<VisitedCountry> user2List = visitedCountryService.getVisitedCountries(user2.getId());
        
        assertThat(user1List.get(0).getColor()).isEqualTo(newColor);
        assertThat(user2List.get(0).getColor()).isEqualTo(emotion.getColorCode());
    }

    @Test
    void testGetVisitedCountries() {
        // given: 테스트용 데이터 생성
        User user = createTestUser("testKakao8");
        Country country = createTestCountry("KR8", "대한민국8", "🇰🇷");
        Emotion emotion = createTestEmotion("행복8", "#FFD700");
        
        // 방문 국가 등록
        visitedCountryService.registerVisitedCountry(user.getId(), country.getCountryCode(), emotion.getId());
        
        // when: 방문 국가 목록 조회
        List<VisitedCountry> visitedList = visitedCountryService.getVisitedCountries(user.getId());
        
        // then: 조회된 목록 확인
        assertThat(visitedList).hasSize(1);
        assertThat(visitedList.get(0).getUser().getId()).isEqualTo(user.getId());
        assertThat(visitedList.get(0).getCountry().getCountryCode()).isEqualTo(country.getCountryCode());
    }

    // 헬퍼 메서드들
    private User createTestUser(String kakaoId) {
        User user = new User();
        user.setKakaoId(kakaoId);
        user.setFirstName("Hong");
        user.setSurName("Gil");
        user.setStatus(true);
        user.setCreatedAt(LocalDate.now());
        userRepository.save(user);
        return user;
    }

    private Country createTestCountry(String countryCode, String countryName, String emoji) {
        Country country = new Country();
        country.setCountryCode(countryCode);
        country.setCountryName(countryName);
        country.setEmoji(emoji);
        countryRepository.save(country);
        return country;
    }

    private Emotion createTestEmotion(String name, String colorCode) {
        Emotion emotion = new Emotion();
        emotion.setName(name);
        emotion.setColorCode(colorCode);
        emotionRepository.save(emotion);
        return emotion;
    }
} 