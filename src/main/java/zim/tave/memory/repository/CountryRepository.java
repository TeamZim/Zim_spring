package zim.tave.memory.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zim.tave.memory.domain.Country;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CountryRepository {

    private final EntityManager em;

    public void save(Country country) {
        em.persist(country);
    }

    public Country findByCode(String countryCode) {
        return em.find(Country.class, countryCode);
    }

    public List<Country> findAll() {
        return em.createQuery("SELECT c FROM Country c", Country.class).getResultList();
    }
//나라 검색용
    public List<Country> findByNameContaining(String keyword) {
        return em.createQuery("SELECT c FROM Country c WHERE c.countryName LIKE :keyword", Country.class)
                .setParameter("keyword", "%" + keyword + "%")
                .getResultList();
    }

    public void delete(Country country) {
        em.remove(em.contains(country) ? country : em.merge(country));
    }
}
