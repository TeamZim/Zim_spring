package zim.tave.memory.test;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TripController {

    private final TripRepository tripRepository;

    @PostMapping("/trips")
    public Trip createTrip(@RequestBody Trip trip) {
        return tripRepository.save(trip);
    }

    @GetMapping("/trips")
    public List<Trip> getTrips() {
        return tripRepository.findAll();
    }
}
