package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.domain.Diary;
import zim.tave.memory.domain.DiaryImage;
import zim.tave.memory.domain.DiaryImage.CameraType;
import zim.tave.memory.dto.CreateDiaryRequest;
import zim.tave.memory.dto.DiaryResponseDto;
import zim.tave.memory.dto.UpdateDiaryOptionalFieldsRequest;
import zim.tave.memory.dto.UpdateRepresentativeImageRequest;
import zim.tave.memory.service.DiaryService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<DiaryResponseDto> createDiary(@RequestBody CreateDiaryRequest request) {
        // 도시 검증
        if (request.getCity() == null || request.getCity().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 날짜 검증
        if (request.getDateTime() == null) {
            return ResponseEntity.badRequest().build();
        }

        // 내용 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 이미지 2장 있는지 확인
        if (request.getImages() == null || request.getImages().size() != 2) {
            return ResponseEntity.badRequest().build();
        }

        // FRONT/BACK 카메라 있는지
        boolean hasFrontCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == CameraType.FRONT);
        boolean hasBackCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == CameraType.BACK);

        if (!hasFrontCamera || !hasBackCamera) {
            return ResponseEntity.badRequest().build();
        }

        // 대표 이미지가 정확히 1개인지
        long representativeCount = request.getImages().stream()
                .filter(CreateDiaryRequest.DiaryImageInfo::isRepresentative)
                .count();

        if (representativeCount != 1) {
            return ResponseEntity.badRequest().build();
        }

        if (request.getCountryCode() == null || request.getCountryCode().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }

        // 정상 로직
        try {
            Diary diary = diaryService.createDiary(request);
            return ResponseEntity.ok(DiaryResponseDto.from(diary));
        } catch (IllegalArgumentException e) {
            // 잘못된 데이터로 인한 예외 (사용자/여행/국가 등을 찾을 수 없음)
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            // 기타 예외
            return ResponseEntity.internalServerError().build();
        }
    }


    @PutMapping("/{diaryId}/optional-fields")
    public ResponseEntity<Void> updateDiaryOptionalFields(@PathVariable Long diaryId,
                                                         @RequestBody UpdateDiaryOptionalFieldsRequest request) {
        diaryService.updateDiaryOptionalFields(diaryId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{diaryId}/representative-image")
    public ResponseEntity<Void> updateRepresentativeImage(@PathVariable Long diaryId,
                                                         @RequestBody UpdateRepresentativeImageRequest request) {
        if (request.getImageId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        diaryService.updateRepresentativeImage(diaryId, request.getImageId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<DiaryResponseDto>> getAllDiaries() {
        List<Diary> diaries = diaryService.findAll();
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<DiaryResponseDto> getDiary(@PathVariable Long diaryId) {
        Diary diary = diaryService.findOne(diaryId);
        return ResponseEntity.ok(DiaryResponseDto.from(diary));
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<DiaryResponseDto>> getDiariesByTripId(@PathVariable Long tripId) {
        List<Diary> diaries = diaryService.findByTripId(tripId);
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<DiaryResponseDto>> getDiariesByUserId(@PathVariable Long userId) {
        List<Diary> diaries = diaryService.findByUserId(userId);
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
} 