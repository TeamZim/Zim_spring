package zim.tave.memory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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
@Tag(name = "Diary", description = "일기 관리 API")
public class DiaryController {

    private final DiaryService diaryService;

    @PostMapping
    @Operation(summary = "일기 생성", description = "새로운 일기를 생성합니다. 정면/후면 카메라 사진 각 1장씩, 총 2장의 이미지가 필요하며 그 중 1장을 대표 이미지로 설정해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 생성 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (필수 필드 누락, 이미지 개수 오류, 카메라 타입 누락 등)", content = @Content),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<DiaryResponseDto> createDiary(@RequestBody CreateDiaryRequest request) {
        System.out.println("=== 일기 생성 요청 시작 ===");
        System.out.println("Request: " + request);
        
        // 도시 검증
        if (request.getCity() == null || request.getCity().trim().isEmpty()) {
            System.out.println("검증 실패: 도시 정보 누락");
            return ResponseEntity.badRequest().build();
        }

        // 날짜 검증
        if (request.getDateTime() == null) {
            System.out.println("검증 실패: 날짜 정보 누락");
            return ResponseEntity.badRequest().build();
        }

        // 내용 검증
        if (request.getContent() == null || request.getContent().trim().isEmpty()) {
            System.out.println("검증 실패: 내용 누락");
            return ResponseEntity.badRequest().build();
        }

        // 이미지 2장 있는지 확인
        if (request.getImages() == null || request.getImages().size() != 2) {
            System.out.println("검증 실패: 이미지 개수 오류. 현재 개수: " + 
                (request.getImages() == null ? "null" : request.getImages().size()));
            return ResponseEntity.badRequest().build();
        }

        // FRONT/BACK 카메라 있는지
        boolean hasFrontCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == CameraType.FRONT);
        boolean hasBackCamera = request.getImages().stream()
                .anyMatch(img -> img.getCameraType() == CameraType.BACK);

        if (!hasFrontCamera || !hasBackCamera) {
            System.out.println("검증 실패: 카메라 타입 오류. FRONT: " + hasFrontCamera + ", BACK: " + hasBackCamera);
            return ResponseEntity.badRequest().build();
        }

        // 대표 이미지가 정확히 1개인지
        long representativeCount = request.getImages().stream()
                .filter(CreateDiaryRequest.DiaryImageInfo::isRepresentative)
                .count();

        if (representativeCount != 1) {
            System.out.println("검증 실패: 대표 이미지 개수 오류. 현재 개수: " + representativeCount);
            return ResponseEntity.badRequest().build();
        }

        if (request.getCountryCode() == null || request.getCountryCode().trim().isEmpty()) {
            System.out.println("검증 실패: 국가 코드 누락");
            return ResponseEntity.badRequest().build();
        }
        
        System.out.println("모든 검증 통과, 서비스 호출");

        // 정상 로직
        try {
            Diary diary = diaryService.createDiary(request);
            return ResponseEntity.ok(DiaryResponseDto.from(diary));
        } catch (IllegalArgumentException e) {
            System.out.println("일기 생성 실패: " + e.getMessage());
            // 구체적인 에러 메시지를 로그로 남기고 클라이언트에게는 간단한 오류 반환
            if (e.getMessage().contains("국가 코드를 찾을 수 없습니다")) {
                System.out.println("존재하지 않는 국가 코드: " + request.getCountryCode());
            } else if (e.getMessage().contains("사용자를 찾을 수 없습니다")) {
                System.out.println("존재하지 않는 사용자");
            } else if (e.getMessage().contains("여행을 찾을 수 없습니다")) {
                System.out.println("존재하지 않는 여행");
            } else if (e.getMessage().contains("감정을 찾을 수 없습니다")) {
                System.out.println("존재하지 않는 감정");
            } else if (e.getMessage().contains("날씨를 찾을 수 없습니다")) {
                System.out.println("존재하지 않는 날씨");
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("일기 생성 중 예상치 못한 오류: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @PutMapping("/{diaryId}/optional-fields")
    @Operation(summary = "일기 선택 필드 수정", description = "일기의 선택적 필드들(감정, 날씨 등)을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 수정 성공"),
            @ApiResponse(responseCode = "404", description = "일기를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> updateDiaryOptionalFields(
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId,
            @RequestBody UpdateDiaryOptionalFieldsRequest request) {
        diaryService.updateDiaryOptionalFields(diaryId, request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{diaryId}/representative-image")
    @Operation(summary = "대표 이미지 변경", description = "일기의 대표 이미지를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대표 이미지 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
            @ApiResponse(responseCode = "404", description = "일기 또는 이미지를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> updateRepresentativeImage(
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId,
            @RequestBody UpdateRepresentativeImageRequest request) {
        if (request.getImageId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        diaryService.updateRepresentativeImage(diaryId, request.getImageId());
        return ResponseEntity.ok().build();
    }

    @GetMapping
    @Operation(summary = "전체 일기 목록 조회", description = "모든 일기의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "일기 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = DiaryResponseDto.class)))
    public ResponseEntity<List<DiaryResponseDto>> getAllDiaries() {
        List<Diary> diaries = diaryService.findAll();
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }

    @GetMapping("/{diaryId}")
    @Operation(summary = "일기 상세 조회", description = "특정 일기의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 조회 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "일기를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<DiaryResponseDto> getDiary(
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId) {
        Diary diary = diaryService.findOne(diaryId);
        return ResponseEntity.ok(DiaryResponseDto.from(diary));
    }

    @GetMapping("/trip/{tripId}")
    @Operation(summary = "여행별 일기 목록 조회", description = "특정 여행에 속한 모든 일기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 일기 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "여행을 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<List<DiaryResponseDto>> getDiariesByTripId(
            @Parameter(description = "여행 ID", required = true) @PathVariable Long tripId) {
        List<Diary> diaries = diaryService.findByTripId(tripId);
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 일기 목록 조회", description = "특정 사용자의 모든 일기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 일기 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<List<DiaryResponseDto>> getDiariesByUserId(
            @Parameter(description = "사용자 ID", required = true) @PathVariable Long userId) {
        List<Diary> diaries = diaryService.findByUserId(userId);
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }

    @DeleteMapping("/{diaryId}")
    @Operation(summary = "일기 삭제", description = "특정 일기를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "일기를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> deleteDiary(
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId) {
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
} 