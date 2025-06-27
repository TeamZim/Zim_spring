package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.LoginRequestDto;
import zim.tave.memory.dto.LoginResponseDto;
import zim.tave.memory.kakao.KakaoApiClient;
import zim.tave.memory.kakao.KakaoUserInfo;
import zim.tave.memory.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final KakaoApiClient kakaoApiClient;

    public LoginResponseDto login(LoginRequestDto request) {
        KakaoUserInfo kakaoUserInfo = kakaoApiClient.getKakaoUserInfo(request.getAccessToken());
        User user = userRepository.findByKakaoId(kakaoUserInfo.getKakaoId()).orElse(null);

        if(user != null) {
            return new LoginResponseDto(false, user.getKakaoId(), user.getProfileImageUrl());
        } else {
            //첫 로그인 시 join으로 이동하여 추가 정보 입력
            return new LoginResponseDto(true, kakaoUserInfo.getKakaoId(), kakaoUserInfo.getProfileImageUrl());
        }
    }
}
