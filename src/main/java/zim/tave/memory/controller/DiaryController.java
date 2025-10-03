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
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.domain.Diary;
import zim.tave.memory.domain.DiaryImage;
import zim.tave.memory.domain.DiaryImage.CameraType;
import zim.tave.memory.dto.CreateDiaryRequest;
import zim.tave.memory.dto.DiaryResponseDto;
import zim.tave.memory.dto.UpdateDiaryOptionalFieldsRequest;
import zim.tave.memory.dto.UpdateRepresentativeImageRequest;
import zim.tave.memory.security.CustomUserDetails;
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
    @Operation(summary = "일기 생성", description = "JWT 토큰으로 인증된 사용자의 일기를 생성합니다. 정면/후면 카메라 사진 각 1장씩, 총 2장의 이미지가 필요하며 그 중 1장을 대표 이미지로 설정해야 합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 생성 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터 (필수 필드 누락, 이미지 개수 오류, 카메라 타입 누락 등)", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<DiaryResponseDto> createDiary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @RequestBody CreateDiaryRequest request) {
        Long userId = userDetails.getUserId();
        request.setUserId(userId); // JWT에서 추출한 userId를 request에 설정
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

    // 일기 선택 필드 수정 - JWT 인증으로 본인 일기만 수정 가능
    @PutMapping("/{diaryId}/optional-fields")
    @Operation(summary = "일기 선택 필드 수정", description = "JWT 토큰으로 인증된 사용자의 일기 선택적 필드들(감정, 날씨 등)을 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 수정 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @ApiResponse(responseCode = "403", description = "권한 없음 (다른 사용자의 일기)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "일기를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> updateDiaryOptionalFields(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId,
            @RequestBody UpdateDiaryOptionalFieldsRequest request) {
        Long userId = userDetails.getUserId();
        Diary diary = diaryService.findOne(diaryId);
        
        // 본인의 일기인지 확인
        if (!diary.getUser().getId().equals(userId)) {
            System.out.println("권한 없음: 사용자 " + userId + "가 다른 사용자의 일기 " + diaryId + " 수정 시도");
            return ResponseEntity.status(403).build();
        }
        
        diaryService.updateDiaryOptionalFields(diaryId, request);
        return ResponseEntity.ok().build();
    }

    // 대표 이미지 변경 - JWT 인증으로 본인 일기만 수정 가능
    @PutMapping("/{diaryId}/representative-image")
    @Operation(summary = "대표 이미지 변경", description = "JWT 토큰으로 인증된 사용자의 일기 대표 이미지를 변경합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대표 이미지 변경 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @ApiResponse(responseCode = "403", description = "권한 없음 (다른 사용자의 일기)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "일기 또는 이미지를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> updateRepresentativeImage(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId,
            @RequestBody UpdateRepresentativeImageRequest request) {
        if (request.getImageId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        Long userId = userDetails.getUserId();
        Diary diary = diaryService.findOne(diaryId);
        
        // 본인의 일기인지 확인
        if (!diary.getUser().getId().equals(userId)) {
            System.out.println("권한 없음: 사용자 " + userId + "가 다른 사용자의 일기 " + diaryId + " 이미지 변경 시도");
            return ResponseEntity.status(403).build();
        }
        
        diaryService.updateRepresentativeImage(diaryId, request.getImageId());
        return ResponseEntity.ok().build();
    }

    // 사용자별 전체 일기 목록 조회 - JWT 인증으로 개인 데이터 보호
    @GetMapping
    @Operation(summary = "내 전체 일기 목록 조회", description = "JWT 토큰으로 인증된 사용자의 모든 일기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content())
    })
    public ResponseEntity<List<DiaryResponseDto>> getMyAllDiaries(
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<Diary> diaries = diaryService.findByUserId(userId);
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }

    // 특정 일기 상세 조회 - JWT 인증으로 본인 일기만 조회 가능하도록 보안 강화
    @GetMapping("/{diaryId}")
    @Operation(summary = "일기 상세 조회", description = "JWT 토큰으로 인증된 사용자의 특정 일기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 조회 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @ApiResponse(responseCode = "403", description = "권한 없음 (다른 사용자의 일기)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "일기를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<DiaryResponseDto> getDiary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId) {
        Long userId = userDetails.getUserId();
        Diary diary = diaryService.findOne(diaryId);
        
        // 본인의 일기인지 확인
        if (!diary.getUser().getId().equals(userId)) {
            System.out.println("권한 없음: 사용자 " + userId + "가 다른 사용자의 일기 " + diaryId + " 접근 시도");
            return ResponseEntity.status(403).build();
        }
        
        return ResponseEntity.ok(DiaryResponseDto.from(diary));
    }

    // 내 여행별 일기 목록 조회 - JWT 인증으로 본인 여행의 일기만 조회
    @GetMapping("/trip/{tripId}")
    @Operation(summary = "내 여행별 일기 목록 조회", description = "JWT 토큰으로 인증된 사용자의 특정 여행에 속한 모든 일기를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 일기 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = DiaryResponseDto.class))),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @ApiResponse(responseCode = "404", description = "여행을 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<List<DiaryResponseDto>> getMyDiariesByTripId(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "여행 ID", required = true) @PathVariable Long tripId) {
        Long userId = userDetails.getUserId();
        List<Diary> diaries = diaryService.findByTripId(tripId);
        
        // 본인의 여행인지 확인 (첫 번째 일기의 사용자 확인)
        if (!diaries.isEmpty() && !diaries.get(0).getUser().getId().equals(userId)) {
            System.out.println("권한 없음: 사용자 " + userId + "가 다른 사용자의 여행 " + tripId + " 일기 접근 시도");
            return ResponseEntity.status(403).build();
        }
        
        List<DiaryResponseDto> diaryDtos = diaries.stream()
                .map(DiaryResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(diaryDtos);
    }


    // 일기 삭제 - JWT 인증으로 본인 일기만 삭제 가능
    @DeleteMapping("/{diaryId}")
    @Operation(summary = "일기 삭제", description = "JWT 토큰으로 인증된 사용자의 특정 일기를 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "일기 삭제 성공"),
            @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
            @ApiResponse(responseCode = "403", description = "권한 없음 (다른 사용자의 일기)", content = @Content()),
            @ApiResponse(responseCode = "404", description = "일기를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> deleteDiary(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Parameter(description = "일기 ID", required = true) @PathVariable Long diaryId) {
        Long userId = userDetails.getUserId();
        Diary diary = diaryService.findOne(diaryId);
        
        // 본인의 일기인지 확인
        if (!diary.getUser().getId().equals(userId)) {
            System.out.println("권한 없음: 사용자 " + userId + "가 다른 사용자의 일기 " + diaryId + " 삭제 시도");
            return ResponseEntity.status(403).build();
        }
        
        diaryService.deleteDiary(diaryId);
        return ResponseEntity.ok().build();
    }
} 