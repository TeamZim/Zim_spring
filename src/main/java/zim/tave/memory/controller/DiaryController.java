package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.domain.Diary;
import zim.tave.memory.domain.DiaryImage;
import zim.tave.memory.dto.CreateDiaryRequest;
import zim.tave.memory.dto.UpdateDiaryOptionalFieldsRequest;
import zim.tave.memory.dto.UpdateRepresentativeImageRequest;
import zim.tave.memory.service.DiaryService;

import java.util.List;

@RestController
@RequestMapping("/api/diaries")
@RequiredArgsConstructor
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    public ResponseEntity<Diary> createDiary(@RequestBody CreateDiaryRequest request) {
        // DTO 검증
        if (request.getCity() == null || request.getCity().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getDateTime() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        // 이미지 검증 (전면/후면 카메라 2장)
        if (request.getImages() == null || request.getImages().size() != 2) {
            return ResponseEntity.badRequest().build();
        }
        
        // 전면/후면 카메라 각각 1장씩 있는지 확인
        boolean hasFrontCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == DiaryImage.CameraType.FRONT);
        boolean hasBackCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == DiaryImage.CameraType.BACK);
        
        if (!hasFrontCamera || !hasBackCamera) {
            return ResponseEntity.badRequest().build();
        }
        
        // 대표사진이 정확히 1개 선택되었는지 확인
        long representativeCount = request.getImages().stream()
                .filter(CreateDiaryRequest.DiaryImageInfo::isRepresentative)
                .count();
        
        if (representativeCount != 1) {
            return ResponseEntity.badRequest().build();
        }
        
        if (request.getCountryCode() == null || request.getCountryCode().trim().isEmpty()) {
            return ResponseEntity.badRequest().build();
        }
        
        Diary diary = diaryService.createDiary(request);
        return ResponseEntity.ok(diary);
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
    public ResponseEntity<List<Diary>> getAllDiaries() {
        List<Diary> diaries = diaryService.findAll();
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/{diaryId}")
    public ResponseEntity<Diary> getDiary(@PathVariable Long diaryId) {
        Diary diary = diaryService.findOne(diaryId);
        return ResponseEntity.ok(diary);
    }

    @GetMapping("/trip/{tripId}")
    public ResponseEntity<List<Diary>> getDiariesByTripId(@PathVariable Long tripId) {
        List<Diary> diaries = diaryService.findByTripId(tripId);
        return ResponseEntity.ok(diaries);
    }

    @GetMapping("/user/{userId}")
    public ResponseEntity<List<Diary>> getDiariesByUserId(@PathVariable Long userId) {
        List<Diary> diaries = diaryService.findByUserId(userId);
        return ResponseEntity.ok(diaries);
    }

    @DeleteMapping("/{diaryId}")
    public ResponseEntity<Void> deleteDiary(@PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
} 