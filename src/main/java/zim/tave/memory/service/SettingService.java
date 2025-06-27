package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.User;
import zim.tave.memory.repository.UserRepository;

@Service
@RequiredArgsConstructor
public class SettingService {

    private final UserRepository userRepository;

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
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        userRepository.delete(user);

    }
}
