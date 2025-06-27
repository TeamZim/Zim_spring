package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.MyPageResponseDto;
//import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.UserRepository;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    //private final DiaryRepository diaryRepository;
    //private final VisitedCountryRespository visitedCountryRespository;

    public MyPageResponseDto getMyPage(Long userId) {
        //유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        // UserInfo
        MyPageResponseDto.UserInfo userInfo = new MyPageResponseDto.UserInfo(
                user.getId(),
                user.getProfileImageUrl(),
                user.getSurName(),
                user.getFirstName(),
                user.getKoreanName(),
                user.getBirth(),
                user.getNationality()
        );
        /*

        //Statistics
        int diaryCount = diaryRepository.countByUserId(userId);
        int countryCount = visitedCountryRepository.countByUserId(userId);
        MyPageResponseDto.Statistics statistics = new MyPageResponseDto.Statistics(countryCount, diaryCount);

        // Flags
        List<String> countryCodes = visitedCountryRepository.findCountryCodesByUserId(userId);
        String flags = countryCodes.stream()
                .map(this::countryCodeToEmoji)
                .collect(Collectors.joining());

        return new MyPageResponseDto(userInfo, statistics, flags);
        */
        return null;
    }
    private String countryCodeToEmoji(String countryCode) {
        // ISO Alpha-2 코드 기반 이모지 변환
        return countryCode.toUpperCase()
                .chars()
                .mapToObj(c -> String.valueOf((char)(c + 127397)))
                .collect(Collectors.joining());
    }
}
