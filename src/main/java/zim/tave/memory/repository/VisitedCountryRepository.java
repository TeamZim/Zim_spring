package zim.tave.memory.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zim.tave.memory.domain.VisitedCountry;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class VisitedCountryRepository {

    private final EntityManager em;

    public void save(VisitedCountry visitedCountry) {
        em.persist(visitedCountry);
    }

    public List<VisitedCountry> findByUserId(Long userId) {
        return em.createQuery("SELECT v FROM VisitedCountry v WHERE v.user.id = :userId", VisitedCountry.class)
                .setParameter("userId", userId)
                .getResultList();
    }
///지도에 색을 칠했는지 확인
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
}
