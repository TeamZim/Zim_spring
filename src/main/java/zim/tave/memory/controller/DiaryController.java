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
            System.out.println("[400] city 누락");
            return ResponseEntity.badRequest().build();
        }

        // 날짜 검증
        if (request.getDateTime() == null) {
            System.out.println("[400] dateTime 누락");
            return ResponseEntity.badRequest().build();
        }

        // 내용 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            System.out.println("[400] content 누락");
            return ResponseEntity.badRequest().build();
        }

        // 이미지 2장 있는지 확인
        if (request.getImages() == null || request.getImages().size() != 2) {
            System.out.println("[400] 이미지 개수 오류");
            return ResponseEntity.badRequest().build();
        }

        // FRONT/BACK 카메라 있는지
        boolean hasFrontCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == CameraType.FRONT);
        boolean hasBackCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == CameraType.BACK);

        if (!hasFrontCamera || !hasBackCamera) {
            System.out.println("[400] FRONT 또는 BACK 이미지 누락");
            return ResponseEntity.badRequest().build();
        }

        // 대표 이미지가 정확히 1개인지
        long representativeCount = request.getImages().stream()
                .filter(CreateDiaryRequest.DiaryImageInfo::isRepresentative)
                .count();

        if (representativeCount != 1) {
            System.out.println("[400] 대표 이미지 개수 오류 (현재: " + representativeCount + ")");
            return ResponseEntity.badRequest().build();
        }

        if (request.getCountryCode() == null || request.getCountryCode().trim().isEmpty()) {
            System.out.println("[400] countryCode 누락");
            return ResponseEntity.badRequest().build();
        }

        // 정상 로직
        System.out.println("[200] 요청 통과, 다이어리 생성 시작");
        Diary diary = diaryService.createDiary(request);
        return ResponseEntity.ok(DiaryResponseDto.from(diary));
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