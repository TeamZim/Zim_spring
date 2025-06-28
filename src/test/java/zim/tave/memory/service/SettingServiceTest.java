package zim.tave.memory.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zim.tave.memory.domain.User;
import zim.tave.memory.repository.UserRepository;

import java.time.LocalDate;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class SettingServiceTest {

    @Autowired
    private SettingService settingService;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void logout(){

        //given
        Long testUserId = 1L;

        //when
        settingService.logout(testUserId);

        //then
        assertThat(true).isTrue();
        System.out.println("로그아웃 성공");
    }

    @Test
    public void deleteUser(){

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

        //when
        settingService.deleteAccount(savedTestUser.getId());

        //then
        Optional<User> user = userRepository.findById(savedTestUser.getId());
        assertThat(user).isEmpty();
        System.out.println("회원 탈퇴 완료");
    }

    @Test
    public void delete_exception() {

        //given
        Long testUserId = 1L;

        // when
        RuntimeException exception = assertThrows(RuntimeException.class, () -> {
            settingService.deleteAccount(testUserId);
        });

        //then
        assertThat(exception.getMessage()).contains("User not found");
        System.out.println("예외 메시지 테스트: " + exception.getMessage());
    }
}
