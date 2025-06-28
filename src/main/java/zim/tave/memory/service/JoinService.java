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

        if(userRepository.findByKakaoId(requestDto.getKakaoId()).isPresent()) {
            //이미 가입된 유저 예외처리 코드 필요
            throw new RuntimeException("(중복)Kakao user already exists");
        }

        User user = new User();
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