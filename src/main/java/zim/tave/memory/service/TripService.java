package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Diary;
import zim.tave.memory.domain.DiaryImage;
import zim.tave.memory.domain.Trip;
import zim.tave.memory.domain.TripTheme;
import zim.tave.memory.domain.User;
import zim.tave.memory.dto.CreateTripRequest;
import zim.tave.memory.dto.UpdateTripRequest;
import zim.tave.memory.repository.DiaryRepository;
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
    private final DiaryRepository diaryRepository;

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
        
        if (request.getRepresentativeImageUrl() != null) {
            findTrip.setRepresentativeImageUrl(request.getRepresentativeImageUrl());
        }
        
        if (request.getStartDate() != null) {
            findTrip.setStartDate(request.getStartDate());
        }
        
        if (request.getEndDate() != null) {
            findTrip.setEndDate(request.getEndDate());
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

    // 여행의 대표 사진 설정
    @Transactional
    public void updateTripRepresentativeImage(Long tripId, Long imageId) {
        Trip trip = tripRepository.findOne(tripId);
        
        // 해당 이미지가 이 여행에 속하는 다이어리의 대표 사진인지 검증
        DiaryImage diaryImage = findDiaryImageById(imageId);
        
        if (!diaryImage.getDiary().getTrip().getId().equals(tripId)) {
            throw new IllegalArgumentException("해당 이미지는 이 여행에 속하지 않습니다.");
        }
        
        if (!diaryImage.isRepresentative()) {
            throw new IllegalArgumentException("선택된 이미지는 다이어리의 대표 사진이 아닙니다.");
        }
        
        trip.setRepresentativeImageUrl(diaryImage.getImageUrl());
    }

    // DiaryImage ID로 DiaryImage 찾기
    private DiaryImage findDiaryImageById(Long imageId) {
        List<Diary> allDiaries = diaryRepository.findAll();
        return allDiaries.stream()
                .flatMap(diary -> diary.getDiaryImages().stream())
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다. ID: " + imageId));
    }
}
