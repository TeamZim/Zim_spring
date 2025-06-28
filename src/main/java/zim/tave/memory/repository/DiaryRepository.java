package zim.tave.memory.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zim.tave.memory.domain.Diary;
import zim.tave.memory.domain.Trip;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class DiaryRepository {

    private final EntityManager em;

    public void save(Diary diary) {
        if (diary.getId() == null) {
            em.persist(diary);
        } else {
            em.merge(diary);
        }
    }

    public Diary findById(Long id) {
        return em.find(Diary.class, id);
    }

    public List<Diary> findAll() {
        return em.createQuery("select d from Diary d", Diary.class).getResultList();
    }

    public List<Diary> findByTripId(Long tripId) {
        return em.createQuery("select d from Diary d where d.trip.id = :tripId", Diary.class)
                .setParameter("tripId", tripId)
                .getResultList();
    }

    public List<Diary> findByUserId(Long userId) {
        return em.createQuery("select d from Diary d where d.user.id = :userId", Diary.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public void delete(Diary diary) {
        em.remove(diary);
    }
}
