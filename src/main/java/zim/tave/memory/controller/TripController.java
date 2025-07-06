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
import zim.tave.memory.domain.Trip;
import zim.tave.memory.dto.CreateTripRequest;
import zim.tave.memory.dto.TripRepresentativeImageDto;
import zim.tave.memory.dto.TripResponseDto;
import zim.tave.memory.dto.UpdateRepresentativeImageRequest;
import zim.tave.memory.dto.UpdateTripRequest;
import zim.tave.memory.service.DiaryService;
import zim.tave.memory.service.TripService;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/trips")
@RequiredArgsConstructor
@Tag(name = "Trip", description = "여행 관리 API")
public class TripController {

    private final TripService tripService;
    private final DiaryService diaryService;

    @PostMapping
    @Operation(summary = "여행 생성", description = "새로운 여행을 생성합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 생성 성공",
                    content = @Content(schema = @Schema(implementation = TripResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content)
    })
    public ResponseEntity<TripResponseDto> createTrip(@RequestBody CreateTripRequest request) {
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
        return ResponseEntity.ok(TripResponseDto.from(trip));
    }

    @PutMapping("/{tripId}")
    @Operation(summary = "여행 수정", description = "기존 여행 정보를 수정합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 수정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
            @ApiResponse(responseCode = "404", description = "여행을 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> updateTrip(
            @Parameter(description = "여행 ID", required = true) @PathVariable Long tripId, 
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
    @Operation(summary = "전체 여행 목록 조회", description = "모든 여행의 목록을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "여행 목록 조회 성공",
            content = @Content(schema = @Schema(implementation = TripResponseDto.class)))
    public ResponseEntity<List<TripResponseDto>> getAllTrips() {
        List<Trip> trips = tripService.findAll();
        List<TripResponseDto> tripDtos = trips.stream()
                .map(TripResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tripDtos);
    }

    @GetMapping("/{tripId}")
    @Operation(summary = "여행 상세 조회", description = "특정 여행의 상세 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 조회 성공",
                    content = @Content(schema = @Schema(implementation = TripResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "여행을 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<TripResponseDto> getTrip(
            @Parameter(description = "여행 ID", required = true) @PathVariable Long tripId) {
        Trip trip = tripService.findOne(tripId);
        return ResponseEntity.ok(TripResponseDto.from(trip));
    }

    @GetMapping("/user/{userId}")
    @Operation(summary = "사용자별 여행 목록 조회", description = "특정 사용자의 모든 여행을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "사용자 여행 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = TripResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<List<TripResponseDto>> getTripsByUserId(
            @Parameter(description = "사용자 ID", required = true) @PathVariable Long userId) {
        List<Trip> trips = tripService.findByUserId(userId);
        List<TripResponseDto> tripDtos = trips.stream()
                .map(TripResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(tripDtos);
    }

    @DeleteMapping("/{tripId}")
    @Operation(summary = "여행 삭제", description = "특정 여행을 삭제합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 삭제 성공"),
            @ApiResponse(responseCode = "404", description = "여행을 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> deleteTrip(
            @Parameter(description = "여행 ID", required = true) @PathVariable Long tripId) {
        tripService.deleteTrip(tripId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{tripId}/representative-images")
    @Operation(summary = "여행에 속한 다이어리들의 대표사진 목록 조회", description = "특정 여행에 속한 모든 다이어리들의 대표사진을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "대표사진 조회 성공",
                    content = @Content(schema = @Schema(implementation = TripRepresentativeImageDto.class))),
            @ApiResponse(responseCode = "404", description = "여행을 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<List<TripRepresentativeImageDto>> getRepresentativeImages(
            @Parameter(description = "여행 ID", required = true) @PathVariable Long tripId) {
        List<TripRepresentativeImageDto> representativeImages = diaryService.getRepresentativeImagesByTripId(tripId);
        return ResponseEntity.ok(representativeImages);
    }

    @PutMapping("/{tripId}/representative-image")
    @Operation(summary = "여행 대표사진 설정", description = "여행의 대표사진을 설정합니다. 해당 여행에 속한 다이어리의 대표사진 중에서 선택할 수 있습니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "여행 대표사진 설정 성공"),
            @ApiResponse(responseCode = "400", description = "잘못된 요청 데이터", content = @Content),
            @ApiResponse(responseCode = "404", description = "여행 또는 이미지를 찾을 수 없음", content = @Content)
    })
    public ResponseEntity<Void> updateTripRepresentativeImage(
            @Parameter(description = "여행 ID", required = true) @PathVariable Long tripId,
            @RequestBody UpdateRepresentativeImageRequest request) {
        
        if (request.getImageId() == null) {
            return ResponseEntity.badRequest().build();
        }
        
        try {
            tripService.updateTripRepresentativeImage(tripId, request.getImageId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().build();
        }
    }
} 