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

    //사용자의 모든 방문 국가 목록 조회
    public List<VisitedCountry> getVisitedCountries(Long userId) {
        return visitedCountryRepository.findByUserId(userId);
    }

    //사용자가 이미 특정 국가를 방문했는지 여부
    public boolean alreadyVisited(Long userId, String countryCode) {
        return visitedCountryRepository.existsByUserIdAndCountryCode(userId, countryCode);
    }

    //방문 국가 등록
    public void registerVisitedCountry(Long userId, String countryCode, Long emotionId) {
        if (alreadyVisited(userId, countryCode)) return;

        //User user = userRepository.find(userId);
        Country country = countryRepository.findByCode(countryCode);
        Emotion emotion = emotionRepository.find(emotionId);

        VisitedCountry visited = new VisitedCountry();
        //visited.setUser(user);
        visited.setCountry(country);
        visited.setEmotion(emotion);
        visited.setColor(emotion.getColorCode());

        visitedCountryRepository.save(visited);
    }
}
