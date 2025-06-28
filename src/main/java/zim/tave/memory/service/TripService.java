package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Trip;
import zim.tave.memory.domain.TripTheme;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.CreateTripRequest;
import zim.tave.memory.dto.UpdateTripRequest;
import zim.tave.memory.repository.TripRepository;
import zim.tave.memory.repository.TripThemeRepository;

import java.time.LocalDate;
import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class TripService {

    private final TripRepository tripRepository;
    private final TripThemeRepository tripThemeRepository;

    @Transactional
    public Trip createTrip(CreateTripRequest request) {
        TripTheme theme = tripThemeRepository.findById(request.getThemeId())
                .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));
        
        // User는 실제로는 인증된 사용자에서 가져와야 함
        User user = new User();
        user.setId(request.getUserId());
        
        Trip trip = Trip.createTrip(user, request.getTripName(), request.getDescription(), theme);
        tripRepository.save(trip);
        return trip;
    }

    @Transactional
    public void saveTrip(Trip trip) {
        tripRepository.save(trip);
    }

    @Transactional
    public void updateTrip(Long tripId, UpdateTripRequest request) {
        Trip findTrip = tripRepository.findOne(tripId);
        
        if (request.getTripName() != null) {
            findTrip.setTripName(request.getTripName());
        }
        
        if (request.getDescription() != null) {
            findTrip.setDescription(request.getDescription());
        }
        
        if (request.getThemeId() != null) {
            TripTheme theme = tripThemeRepository.findById(request.getThemeId())
                    .orElseThrow(() -> new IllegalArgumentException("테마를 찾을 수 없습니다."));
            findTrip.setTripTheme(theme);
        }
    }

    public List<Trip> findAll() {
        return tripRepository.findAll();
    }

    public Trip findOne(Long tripId) {
        return tripRepository.findOne(tripId);
    }

    public List<Trip> findByUserId(Long userId) {
        return tripRepository.findByUserId(userId);
    }

    @Transactional
    public void deleteTrip(Long tripId) {
        Trip trip = tripRepository.findOne(tripId);
        trip.setIsDeleted(true);
    }

    // 여행의 마지막 다이어리 날짜를 기준으로 종료 날짜 업데이트
    @Transactional
    public void updateTripEndDate(Long tripId) {
        tripRepository.updateTripEndDate(tripId);
    }

    // 여행의 마지막 다이어리 날짜 조회
    public LocalDate getLastDiaryDateByTripId(Long tripId) {
        return tripRepository.findLastDiaryDateByTripId(tripId);
    }
}
