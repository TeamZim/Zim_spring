package zim.tave.memory.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zim.tave.memory.domain.Emotion;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class EmotionRepository {

    private final EntityManager em;

    public Emotion find(Long id) {
        return em.find(Emotion.class, id);
    }

    public List<Emotion> findAll() {
        return em.createQuery("SELECT e FROM Emotion e", Emotion.class)
                .getResultList();
    }

    public void save(Emotion emotion) {
        em.persist(emotion);
    }
}
