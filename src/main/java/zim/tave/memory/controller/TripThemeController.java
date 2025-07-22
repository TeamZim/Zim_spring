package zim.tave.memory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.domain.TripTheme;
import zim.tave.memory.dto.TripThemeResponseDto;
import zim.tave.memory.repository.TripThemeRepository;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/themes")
@RequiredArgsConstructor
@Tag(name = "TripTheme", description = "여행 테마 관리 API")
public class TripThemeController {

    private final TripThemeRepository tripThemeRepository;

    @GetMapping
    @Operation(summary = "테마 목록 조회", description = "모든 여행 테마 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "테마 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = TripThemeResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "서버 내부 오류", content = @Content)
    })
    public ResponseEntity<List<TripThemeResponseDto>> getAllThemes() {
        List<TripTheme> themes = tripThemeRepository.findAllByOrderById();
        List<TripThemeResponseDto> response = themes.stream()
                .map(TripThemeResponseDto::from)
                .collect(Collectors.toList());
        return ResponseEntity.ok(response);
    }
} 