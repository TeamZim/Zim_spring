package zim.tave.memory.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zim.tave.memory.domain.Trip;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class TripRepository {

    private final EntityManager em;

    public void save(Trip trip) {
        if (trip.getId() == null) {
            em.persist(trip);
        } else {
            em.merge(trip);
        }
    }

    public Trip findOne(Long id) {
        return em.find(Trip.class, id);
    }

    public List<Trip> findAll() {
        return em.createQuery("select t from Trip t", Trip.class).getResultList();
    }

    public List<Trip> findByUserId(Long userId) {
        return em.createQuery("select t from Trip t where t.user.id = :userId", Trip.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // 다이어리가 하나 이상 있는 모든 여행 조회
    public List<Trip> findAllWithDiaries() {
        return em.createQuery("select t from Trip t where size(t.diaries) > 0", Trip.class)
                .getResultList();
    }

    // 다이어리가 하나 이상 있는 특정 사용자의 여행 조회
    public List<Trip> findByUserIdWithDiaries(Long userId) {
        return em.createQuery("select t from Trip t where t.user.id = :userId and size(t.diaries) > 0", Trip.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    // 여행의 마지막 다이어리 날짜를 기준으로 종료 날짜 업데이트
    public void updateTripEndDate(Long tripId) {
        em.createQuery("UPDATE Trip t SET t.endDate = (SELECT CAST(MAX(d.createdAt) AS date) FROM Diary d WHERE d.trip.id = :tripId) WHERE t.id = :tripId")
                .setParameter("tripId", tripId)
                .executeUpdate();
    }

    // 여행의 마지막 다이어리 날짜 조회
    public LocalDate findLastDiaryDateByTripId(Long tripId) {
        return em.createQuery("SELECT CAST(MAX(d.createdAt) AS date) FROM Diary d WHERE d.trip.id = :tripId", LocalDate.class)
                .setParameter("tripId", tripId)
                .getSingleResult();
    }
}
