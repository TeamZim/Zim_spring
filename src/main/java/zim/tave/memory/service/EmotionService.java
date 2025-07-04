package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zim.tave.memory.domain.Emotion;
import zim.tave.memory.repository.EmotionRepository;

import java.util.List;

@Service
@RequiredArgsConstructor
public class EmotionService {
    private final EmotionRepository emotionRepository;

    public List<Emotion> getAllEmotions() {
        return emotionRepository.findAllByOrderById();
    }
} 