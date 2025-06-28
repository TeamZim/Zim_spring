package zim.tave.memory.service;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.JoinRequestDto;
import zim.tave.memory.repository.UserRepository;

import java.time.LocalDate;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
public class JoinServiceTest {

    @Autowired
    private JoinService joinService;

    @Autowired
    private UserRepository userRepository;

    @Test
    void testJoin() {
        //given
        JoinRequestDto requestDto = new JoinRequestDto(
                "testKakaoId000",
                "https://test.com/image.jpg",
                "sur",
                "first",
                "최형원",
                LocalDate.of(2222, 2, 2),
                "REPUBLIC OF KOREA"
        );

        // when
        User savedUser = joinService.join(requestDto);

        // then
        assertThat(savedUser).isNotNull();
        assertThat(savedUser.getKakaoId()).isEqualTo("testKakaoId000");
        assertThat(savedUser.getProfileImageUrl()).isEqualTo("https://test.com/image.jpg");
        assertThat(savedUser.getSurName()).isEqualTo("sur");
        assertThat(savedUser.getFirstName()).isEqualTo("first");
        assertThat(savedUser.getKoreanName()).isEqualTo("최형원");
        assertThat(savedUser.getBirth()).isEqualTo(LocalDate.of(2222, 2, 2));
        assertThat(savedUser.getNationality()).isEqualTo("REPUBLIC OF KOREA");

        assertThat(savedUser.getDiaryCount()).isEqualTo(0);
        assertThat(savedUser.getVisitedCountryCount()).isEqualTo(0);
        assertThat(savedUser.getFlags()).isEqualTo("");

        System.out.println("회원가입 완료: " + savedUser);
    }

    @Test
    public void 중복_회원_예외(){

        // given
        JoinRequestDto dto1 = new JoinRequestDto(
                "duplicateKakaoId",
                "url1",
                "sur1",
                "first1",
                "ㅇㅇ",
                LocalDate.of(2222, 2, 2),
                "KOREA"
        );

        JoinRequestDto dto2 = new JoinRequestDto(
                "duplicateKakaoId",
                "url2",
                "sur2",
                "first2",
                "ㄷㄷ",
                LocalDate.of(2222, 2, 2),
                "KOREA"
        );
        //when
        joinService.join(dto1);
        System.out.println("회원가입 완료: " + dto1);

        // then
        RuntimeException exception = assertThrows(RuntimeException.class, () -> joinService.join(dto2));
        assertThat(exception.getMessage()).contains("(중복)Kakao user already exists");
        System.out.println("예외 발생: " + exception.getMessage());
    }
}