package zim.tave.memory.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zim.tave.memory.domain.VisitedCountry;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VisitedCountryRepository {

    private final EntityManager em;

    public void save(VisitedCountry visitedCountry) {
        em.persist(visitedCountry);
    }

    public Optional<VisitedCountry> findById(Long visitedCountryId) {
        VisitedCountry visitedCountry = em.find(VisitedCountry.class, visitedCountryId);
        return Optional.ofNullable(visitedCountry);
    }

    public List<VisitedCountry> findByUserId(Long userId) {
        return em.createQuery("SELECT v FROM VisitedCountry v WHERE v.user.id = :userId", VisitedCountry.class)
                .setParameter("userId", userId)
                .getResultList();
    }
//지도애 색을 칠했는지 확인
    public boolean existsByUserIdAndCountryCode(Long userId, String countryCode) {
        Long count = em.createQuery(
                        "SELECT COUNT(v) FROM VisitedCountry v WHERE v.user.id = :userId AND v.country.countryCode = :code",
                        Long.class
                )
                .setParameter("userId", userId)
                .setParameter("code", countryCode)
                .getSingleResult();

        return count > 0;
    }

    //사용자가 방문한 나라 국가코드와 userId로 찾기
    public java.util.Optional<VisitedCountry> findByUserIdAndCountryCode(Long userId, String countryCode) {
        List<VisitedCountry> result = em.createQuery(
                        "SELECT v FROM VisitedCountry v WHERE v.user.id = :userId AND v.country.countryCode = :code",
                        VisitedCountry.class
                )
                .setParameter("userId", userId)
                .setParameter("code", countryCode)
                .getResultList();

        return result.isEmpty() ? java.util.Optional.empty() : java.util.Optional.of(result.get(0));
    }

    //사용자의 방문 국가 수 조회
    public Long countByUserId(Long userId) {
        Long count = em.createQuery(
                        "SELECT COUNT(v) FROM VisitedCountry v WHERE v.user.id = :userId",
                        Long.class
                )
                .setParameter("userId", userId)
                .getSingleResult();

        return count;
    }

    //방문 국가 삭제
    public void delete(VisitedCountry visitedCountry) {
        em.remove(em.contains(visitedCountry) ? visitedCountry : em.merge(visitedCountry));
    }

}
