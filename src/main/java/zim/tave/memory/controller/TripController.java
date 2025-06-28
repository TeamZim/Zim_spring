package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.domain.Trip;
import zim.tave.memory.dto.CreateTripRequest;
import zim.tave.memory.dto.UpdateTripRequest;
import zim.tave.memory.service.TripService;

import java.util.List;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
public class TripController {

    private final TripService tripService;

    @PostMapping
    public ResponseEntity<Trip> createTrip(@RequestBody CreateTripRequest request) {
        // DTO 검증
        if (request.getTripName() == null || request.getTripName().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getTripName().trim().length() > 14) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getDescription() != null && request.getDescription().trim().length() > 56) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getThemeId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Trip trip = tripService.createTrip(request);
        return ResponseEntity.ok(trip);
    }

    @PutMapping("/{tripId}")
    public ResponseEntity<Void> updateTrip(@PathVariable Long tripId, 
                                          @RequestBody UpdateTripRequest request) {
        // DTO 검증
        if (request.getTripName() != null && request.getTripName().trim().length() > 14) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getDescription() != null && request.getDescription().trim().length() > 56) {
            return ResponseEntity.badRequest().build();
        }
        
        tripService.updateTrip(tripId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<Trip>> getAllTrips() {
        List<Trip> trips = tripService.findAll();
        return ResponseEntity.ok(trips);
    }

    @GetMapping("/{tripId}")
    public ResponseEntity<Trip> getTrip(@PathVariable Long tripId) {
        Trip trip = tripService.findOne(tripId);
        return ResponseEntity.ok(trip);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Trip>> getTripsByUserId(@PathVariable Long userId) {
        List<Trip> trips = tripService.findByUserId(userId);
        return ResponseEntity.ok(trips);
    }

    @DeleteMapping("/{tripId}")
    public ResponseEntity<Void> deleteTrip(@PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok().build();
    }
} 