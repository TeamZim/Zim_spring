package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.JoinRequestDto;
import zim.tave.memory.repository.UserRepository;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class JoinService {
    private final UserRepository userRepository;

    @Transactional
    public User join(JoinRequestDto requestDto) {

        User user = userRepository.findByKakaoId(requestDto.getKakaoId())
                .orElseThrow(() -> new RuntimeException("카카오 로그인을 먼저 진행해주세요."));

        user.setKakaoId(requestDto.getKakaoId());
        user.setProfileImageUrl(requestDto.getProfileImageUrl());
        user.setSurName(requestDto.getSurName());
        user.setFirstName(requestDto.getFirstName());
        user.setKoreanName(requestDto.getKoreanName());
        user.setBirth(requestDto.getBirth());
        user.setNationality(requestDto.getNationality());
        user.setCreatedAt(LocalDate.now());
        user.setStatus(true);

        //마이페이지 Statistics 정보
        user.setDiaryCount(0L);
        user.setVisitedCountryCount(0L);
        user.setFlags("");

        return userRepository.save(user);
    }
}