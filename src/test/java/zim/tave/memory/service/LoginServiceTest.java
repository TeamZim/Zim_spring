package zim.tave.memory.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.LoginRequestDto;
import zim.tave.memory.dto.LoginResponseDto;
import zim.tave.memory.kakao.KakaoApiClient;
import zim.tave.memory.kakao.KakaoUserInfo;
import zim.tave.memory.repository.UserRepository;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@ExtendWith(MockitoExtension.class)
public class LoginServiceTest {

    @Mock
    private KakaoApiClient kakaoApiClient;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private LoginService loginService;

    @Test
    void testKakaoLogin() {
        // given
        String testAccessToken = "test-token";
        String testKakaoId = "99999";
        String testImageUrl = "https://test.com/img.jpg";

        KakaoUserInfo kakaoUserInfo = new KakaoUserInfo(testKakaoId, testImageUrl);
        Mockito.when(kakaoApiClient.getKakaoUserInfo(testAccessToken))
                .thenReturn(kakaoUserInfo);

        LoginRequestDto request = new LoginRequestDto(testAccessToken);

        // When
        LoginResponseDto response = loginService.login(request);

        // then
        assertThat(response.getKakaoId()).isEqualTo("99999");
        assertThat(response.getProfileImageUrl()).isEqualTo("https://test.com/img.jpg");

        System.out.println("카카오 ID: " + response.getKakaoId());
        System.out.println("프로필 이미지: " + response.getProfileImageUrl());
    }
}
