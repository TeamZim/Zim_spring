package zim.tave.memory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zim.tave.memory.domain.Country;
import zim.tave.memory.domain.Emotion;
import zim.tave.memory.domain.User;
import zim.tave.memory.domain.VisitedCountry;
import zim.tave.memory.repository.CountryRepository;
import zim.tave.memory.repository.EmotionRepository;
import zim.tave.memory.repository.UserRepository;
import zim.tave.memory.repository.VisitedCountryRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class VisitedCountryService {

    private final VisitedCountryRepository visitedCountryRepository;
    private final CountryRepository countryRepository;
    private final UserRepository userRepository;
    private final EmotionRepository emotionRepository;

    //사용자의 모든 방문 국가 목록 조회 (감정 정보 포함)
    public List<VisitedCountry> getVisitedCountries(Long userId) {
        return visitedCountryRepository.findByUserId(userId);
    }

    //방문 국가 색 업데이트
    public void updateVisitedCountryColor(Long userId, String countryCode, String newColor) {
        VisitedCountry visitedCountry = visitedCountryRepository.findByUserIdAndCountryCode(userId, countryCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 국가 기록이 존재하지 않습니다."));

        visitedCountry.setColor(newColor);
        visitedCountryRepository.save(visitedCountry);
    }

    //사용자가 이미 특정 국가를 방문했는지 여부
    public boolean alreadyVisited(Long userId, String countryCode) {
        return visitedCountryRepository.existsByUserIdAndCountryCode(userId, countryCode);
    }

    //방문 국가 등록 (기존 기록이 있으면 감정과 색상 업데이트)
    public void registerVisitedCountry(Long userId, String countryCode, Long emotionId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("해당 사용자가 존재하지 않습니다. id=" + userId));
        Country country = countryRepository.findByCode(countryCode);
        Emotion emotion = emotionRepository.findById(emotionId)
                .orElseThrow(() -> new IllegalArgumentException("감정을 찾을 수 없습니다. ID: " + emotionId));

        // 기존 기록이 있는지 확인
        var existingVisitedCountry = visitedCountryRepository.findByUserIdAndCountryCode(userId, countryCode);
        
        if (existingVisitedCountry.isPresent()) {
            // 기존 기록이 있으면 감정과 색상 업데이트
            VisitedCountry visited = existingVisitedCountry.get();
            visited.setEmotion(emotion);
            visited.setColor(emotion.getColorCode());
            visitedCountryRepository.save(visited);
        } else {
            // 새로운 기록 생성
            VisitedCountry visited = new VisitedCountry();
            visited.setUser(user);
            visited.setCountry(country);
            visited.setEmotion(emotion);
            visited.setColor(emotion.getColorCode());
            visitedCountryRepository.save(visited);
        }
    }

    //특정 사용자의 특정 방문 국가 삭제
    public void deleteVisitedCountry(Long userId, String countryCode) {
        VisitedCountry visitedCountry = visitedCountryRepository.findByUserIdAndCountryCode(userId, countryCode)
                .orElseThrow(() -> new IllegalArgumentException("해당 국가 기록이 존재하지 않습니다."));
        visitedCountryRepository.delete(visitedCountry);
    }
}
