package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.User;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.UserRepository;
import zim.tave.memory.repository.VisitedCountryRepository;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final UserRepository userRepository;
    private final VisitedCountryRepository visitedCountryRepository;
    private final DiaryRepository diaryRepository;

    @Transactional
    public void logout(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if (!user.isStatus()) {
            throw new RuntimeException("User already logged out");
        }
        user.setStatus(false); //로그아웃 시 status false로 설정

        //프론트에서 accessToken 삭제
    }

    @Transactional
    public void deleteAccount(Long userId) {
        // 연관 데이터 먼저 삭제
        visitedCountryRepository.deleteAllByUserId(userId);
        diaryRepository.deleteAllByUserId(userId);

        // 사용자 삭제
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        userRepository.delete(user);
    }
}
