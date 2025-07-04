package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.*;
import zim.tave.memory.dto.CreateDiaryRequest;
import zim.tave.memory.dto.TripRepresentativeImageDto;
import zim.tave.memory.dto.UpdateDiaryOptionalFieldsRequest;
import zim.tave.memory.repository.DiaryRepository;
import zim.tave.memory.repository.TripRepository;
import zim.tave.memory.repository.UserRepository;
import zim.tave.memory.repository.EmotionRepository;
import zim.tave.memory.repository.WeatherRepository;

import java.time.LocalDateTime;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class DiaryService {

    private final UserRepository userRepository;
    private final DiaryRepository diaryRepository;
    private final TripRepository tripRepository;
    private final EmotionRepository emotionRepository;
    private final WeatherRepository weatherRepository;
    private final CountryService countryService;
    private final VisitedCountryService visitedCountryService;

    @Transactional
    public Diary createDiary(CreateDiaryRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Trip trip = tripRepository.findOne(request.getTripId());
        
        // CountryService를 통해 Country 조회
        Country country = countryService.findByCode(request.getCountryCode());
        if (country == null) {
            throw new IllegalArgumentException("국가 코드를 찾을 수 없습니다: " + request.getCountryCode());
        }
        
        // 감정은 필수 필드
        if (request.getEmotionId() == null) {
            throw new IllegalArgumentException("감정은 필수 입력 항목입니다.");
        }
        Emotion emotion = emotionRepository.findById(request.getEmotionId())
                .orElseThrow(() -> new IllegalArgumentException("감정을 찾을 수 없습니다."));
        
        Diary diary = Diary.createDiary(user, trip, country, request.getCity(), 
                                       request.getDateTime(), request.getContent());
        diaryRepository.save(diary);
        
        // 이미지 저장 (전면/후면 카메라)
        if (request.getImages() != null) {
            for (int i = 0; i < request.getImages().size(); i++) {
                CreateDiaryRequest.DiaryImageInfo imageInfo = request.getImages().get(i);
                DiaryImage diaryImage = new DiaryImage();
                diaryImage.setDiary(diary);
                diaryImage.setImageUrl(imageInfo.getImageUrl());
                diaryImage.setCameraType(imageInfo.getCameraType());
                diaryImage.setImageOrder(i + 1);
                diaryImage.setRepresentative(imageInfo.isRepresentative()); // 사용자가 선택한 대표사진
                diary.addDiaryImage(diaryImage);
            }
        }
        
        // 선택적 필드 설정 (감정은 이미 위에서 처리했으므로 제외)
        Weather weather = null;
        if (request.getWeatherId() != null) {
            weather = weatherRepository.findById(request.getWeatherId())
                    .orElseThrow(() -> new IllegalArgumentException("날씨를 찾을 수 없습니다."));
        }
        
        diary.setOptionalFields(request.getDetailedLocation(), request.getAudioUrl(), 
                               emotion, weather);
        
        // Trip의 종료 날짜를 다이어리 생성 날짜로 업데이트
        trip.updateEndDate(diary.getCreatedAt().toLocalDate());
        
        // 방문한 국가를 VisitedCountry에 자동 등록 (감정 포함)
        try {
            visitedCountryService.registerVisitedCountry(user.getId(), country.getCountryCode(), emotion.getId());
        } catch (Exception e) {
            // VisitedCountry 등록 실패 시 로그만 남기고 다이어리 생성은 계속 진행
            System.err.println("VisitedCountry 등록 실패: " + e.getMessage());
        }
        
        return diary;
    }

    @Transactional
    public void updateDiaryOptionalFields(Long diaryId, UpdateDiaryOptionalFieldsRequest request) {
        Diary diary = diaryRepository.findById(diaryId);
        
        Emotion emotion = null;
        if (request.getEmotionId() != null) {
            emotion = emotionRepository.findById(request.getEmotionId())
                    .orElseThrow(() -> new IllegalArgumentException("감정을 찾을 수 없습니다."));
        }
        
        Weather weather = null;
        if (request.getWeatherId() != null) {
            weather = weatherRepository.findById(request.getWeatherId())
                    .orElseThrow(() -> new IllegalArgumentException("날씨를 찾을 수 없습니다."));
        }
        
        diary.setOptionalFields(request.getDetailedLocation(), request.getAudioUrl(), 
                               emotion, weather);
    }

    @Transactional
    public void updateRepresentativeImage(Long diaryId, Long imageId) {
        Diary diary = diaryRepository.findById(diaryId);
        
        // 모든 이미지의 대표사진 설정을 false로 초기화
        diary.getDiaryImages().forEach(img -> img.setRepresentative(false));
        
        // 선택된 이미지를 대표사진으로 설정
        DiaryImage selectedImage = diary.getDiaryImages().stream()
                .filter(img -> img.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다."));
        
        selectedImage.setRepresentative(true);
    }

    @Transactional
    public void saveDiary(Diary diary) {
        diaryRepository.save(diary);
    }

    public List<Diary> findAll() {
        return diaryRepository.findAll();
    }

    public Diary findOne(Long diaryId) {
        return diaryRepository.findById(diaryId);
    }

    public List<Diary> findByTripId(Long tripId) {
        return diaryRepository.findByTripId(tripId);
    }

    public List<Diary> findByUserId(Long userId) {
        return diaryRepository.findByUserId(userId);
    }

    // 여행별 대표사진 조회
    public List<TripRepresentativeImageDto> getRepresentativeImagesByTripId(Long tripId) {
        List<Diary> diaries = diaryRepository.findByTripId(tripId);
        
        return diaries.stream()
                .filter(diary -> diary.getDiaryImages() != null && !diary.getDiaryImages().isEmpty())
                .map(diary -> {
                    // 대표사진 찾기 (대표사진이 없으면 첫 번째 이미지)
                    DiaryImage representativeImage = diary.getDiaryImages().stream()
                            .filter(DiaryImage::isRepresentative)
                            .findFirst()
                            .orElse(diary.getDiaryImages().get(0));
                    
                    return new TripRepresentativeImageDto(
                            diary.getId(),
                            representativeImage.getImageUrl(),
                            diary.getCountry().getCountryCode(),
                            diary.getCountry().getCountryName(),
                            diary.getCity(),
                            diary.getContent()
                    );
                })
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteDiary(Long diaryId) {
        Diary diary = diaryRepository.findById(diaryId);
        Long tripId = diary.getTrip().getId();
        diaryRepository.delete(diary);
        
        // 다이어리 삭제 후 Trip의 종료 날짜를 업데이트
        // 남은 다이어리 중 가장 최근 날짜로 설정
        List<Diary> remainingDiaries = diaryRepository.findByTripId(tripId);
        if (remainingDiaries.isEmpty()) {
            // 다이어리가 없으면 종료 날짜를 null로 설정
            Trip trip = tripRepository.findOne(tripId);
            trip.setEndDate(null);
        } else {
            // 남은 다이어리 중 가장 최근 날짜로 설정
            LocalDate latestDate = remainingDiaries.stream()
                    .map(d -> d.getCreatedAt().toLocalDate())
                    .max(LocalDate::compareTo)
                    .orElse(null);
            Trip trip = tripRepository.findOne(tripId);
            trip.setEndDate(latestDate);
        }
    }

    // DiaryImage ID로 DiaryImage 찾기
    public DiaryImage findDiaryImageById(Long imageId) {
        List<Diary> allDiaries = diaryRepository.findAll();
        return allDiaries.stream()
                .flatMap(diary -> diary.getDiaryImages().stream())
                .filter(image -> image.getId().equals(imageId))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("이미지를 찾을 수 없습니다. ID: " + imageId));
    }
}
