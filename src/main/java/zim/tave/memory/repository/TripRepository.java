package zim.tave.memory.repository;

import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import zim.tave.memory.domain.Trip;

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
}
