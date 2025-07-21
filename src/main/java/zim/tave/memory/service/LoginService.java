package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.LoginRequestDto;
import zim.tave.memory.dto.LoginResponseDto;
import zim.tave.memory.kakao.KakaoApiClient;
import zim.tave.memory.kakao.KakaoUserInfo;
import zim.tave.memory.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class LoginService {

    private final UserRepository userRepository;
    private final KakaoApiClient kakaoApiClient;

    public LoginResponseDto login(LoginRequestDto request) {
        KakaoUserInfo kakaoUserInfo = kakaoApiClient.getKakaoUserInfo(request.getAccessToken());
        User user = userRepository.findByKakaoId(kakaoUserInfo.getKakaoId()).orElse(null);

        if (user != null) {
            // 기존 회원
            return new LoginResponseDto(
                    user.getId(),
                    true,
                    user.getKakaoId(),
                    user.getProfileImageUrl()
            );
        }

        // 처음 로그인한 사용자 → User 생성
        User newUser = new User();
        newUser.setKakaoId(kakaoUserInfo.getKakaoId());
        newUser.setProfileImageUrl(kakaoUserInfo.getProfileImageUrl());
        newUser.setCreatedAt(LocalDate.now());
        newUser.setStatus(true);

        // 기본값 세팅 (join에서 업데이트됨)
        newUser.setSurName(null);
        newUser.setFirstName(null);
        newUser.setKoreanName(null);
        newUser.setBirth(null);
        newUser.setNationality(null);
        newUser.setDiaryCount(0L);
        newUser.setVisitedCountryCount(0L);
        newUser.setFlags("");

        User savedUser = userRepository.save(newUser);

        return new LoginResponseDto(
                savedUser.getId(),
                false,
                savedUser.getKakaoId(),
                savedUser.getProfileImageUrl()
        );
    }
}

