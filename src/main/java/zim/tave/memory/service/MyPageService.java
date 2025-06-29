package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zim.tave.memory.domain.User;
import zim.tave.memory.domain.VisitedCountry;
import zim.tave.memory.dto.MyPageResponseDto;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.UserRepository;
import zim.tave.memory.repository.VisitedCountryRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MyPageService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final VisitedCountryRepository visitedCountryRepository;

    public MyPageResponseDto getMyPage(Long userId) {


        //유저 조회
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        String formattedBirth = birthFormatter(user.getBirth());

        // UserInfo
        MyPageResponseDto.UserInfo userInfo = new MyPageResponseDto.UserInfo(
                user.getId(),
                user.getProfileImageUrl(),
                user.getSurName().toUpperCase(),
                user.getFirstName().toUpperCase(),
                user.getKoreanName(),
                formattedBirth,
                user.getNationality()
        );

        //Statistics
        Long diaryCount = diaryRepository.countByUserId(userId);
        Long countryCount = visitedCountryRepository.countByUserId(userId);
        MyPageResponseDto.Statistics statistics = new MyPageResponseDto.Statistics(countryCount, diaryCount);

        // Flags
        List<VisitedCountry> visitedCountries = visitedCountryRepository.findByUserId(userId);
        String flags = visitedCountries.stream()
                .map(vc -> vc.getCountry().getEmoji())
                .collect(Collectors.joining());

        return new MyPageResponseDto(userInfo, statistics, flags);
    }

    private String countryCodeToEmoji(String countryCode) {
        // ISO Alpha-2 코드 기반 이모지 변환
        return countryCode.toUpperCase()
                .chars()
                .mapToObj(c -> String.valueOf((char)(c + 127397)))
                .collect(Collectors.joining());
    }

    private String birthFormatter(LocalDate birth) {
        int day = birth.getDayOfMonth();
        int year = birth.getYear();

        String[] englishMonthForm = {"Jan", "Feb", "Mar", "Apr", "May", "Jun",
                "Jul", "Aug", "Sep", "Oct", "Nov", "Dec"};
        String[] koreanMonthForm = {"1월", "2월", "3월", "4월", "5월", "6월",
                "7월", "8월", "9월", "10월", "11월", "12월"};

        int monthIndex = birth.getMonthValue() - 1;
        String koreanMonth = koreanMonthForm[monthIndex];
        String englishMonth = englishMonthForm[monthIndex];

        return String.format("%d %s/%s %d", day, koreanMonth, englishMonth, year);
    }
}
