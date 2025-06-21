package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Trip;
import zim.tave.memory.repository.TripRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;

    @Transactional
    public void saveTrip(Trip trip) {
        tripRepository.save(trip);
    }

    @Transactional
    public void updateTrip(Long tripId, String tripName, String content, LocalDate startDate, LocalDate endDate) {
        Trip findTrip = tripRepository.findOne(tripId);
        findTrip.setTripName(tripName);
        findTrip.setContent(content);
        findTrip.setStartDate(startDate);
        findTrip.setEndDate(endDate);
    }

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public Trip findOne(Long tripId) {
        return tripRepository.findOne(tripId);
    }
}
