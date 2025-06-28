package zim.tave.memory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zim.tave.memory.domain.Emotion;

import java.util.List;

public interface EmotionRepository extends JpaRepository<Emotion, Long> {
    
    List<Emotion> findAllByOrderById();
    
    Emotion findByName(String name);
}
