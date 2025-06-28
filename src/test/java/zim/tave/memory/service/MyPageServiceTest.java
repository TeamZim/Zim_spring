package zim.tave.memory.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.MyPageResponseDto;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.UserRepository;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
public class MyPageServiceTest {

    @Autowired
    private MyPageService myPageService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiaryRepository diaryRepository;

    //@Autowired
    //private VisitedCountryRepository visitedCountryRepository;

    @Test
    public void myPageServiceTest() {

        //given
        User testUser = new User();
        testUser.setKakaoId("testKakaoId000");
        testUser.setProfileImageUrl("https://example.com/test.jpg");
        testUser.setSurName("sur");
        testUser.setFirstName("first");
        testUser.setKoreanName("최형원");
        testUser.setBirth(LocalDate.of(2222, 2, 2));
        testUser.setNationality("KOREA");
        testUser.setCreatedAt(LocalDate.now());
        testUser.setStatus(true);
        testUser.setDiaryCount(0);
        testUser.setVisitedCountryCount(0);
        testUser.setFlags("");

        User savedTestUser = userRepository.save(testUser);

        // when
        MyPageResponseDto response = myPageService.getMyPage(savedTestUser.getId());

        // then
        assertThat(savedTestUser).isNotNull();
        assertThat(savedTestUser.getKakaoId()).isEqualTo("testKakaoId000");
        assertThat(savedTestUser.getProfileImageUrl()).isEqualTo("https://example.com/test.jpg");
        assertThat(savedTestUser.getSurName()).isEqualTo("sur");
        assertThat(savedTestUser.getFirstName()).isEqualTo("first");
        assertThat(savedTestUser.getKoreanName()).isEqualTo("최형원");
        assertThat(savedTestUser.getBirth()).isEqualTo(LocalDate.of(2222, 2, 2));
        assertThat(savedTestUser.getNationality()).isEqualTo("KOREA");
        assertThat(savedTestUser.getDiaryCount()).isEqualTo(0);
        assertThat(savedTestUser.getVisitedCountryCount()).isEqualTo(0);
        assertThat(savedTestUser.getFlags()).isEqualTo("");

        System.out.println("마이페이지 조회: " + savedTestUser);
    }
}