package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.service.VisitedCountryService;
import zim.tave.memory.service.CountryService;
import zim.tave.memory.dto.VisitedCountryResponseDto;
import zim.tave.memory.dto.RegisterVisitedCountryRequestDto;
import zim.tave.memory.dto.UpdateVisitedCountryColorRequestDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import zim.tave.memory.domain.Country;
import zim.tave.memory.dto.CountrySearchResponseDto;
import zim.tave.memory.dto.ListResponse;
import zim.tave.memory.security.CustomUserDetails;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
@Tag(name = "Country-Controller", description = "방문 국가 관리")
public class CountryController {

    private final VisitedCountryService visitedCountryService;
    private final CountryService countryService;

    @Operation(summary = "사용자의 방문 국가 전체 조회", description = "JWT 토큰으로 인증된 사용자의 방문 국가와 감정 정보를 조회합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "조회 성공",
            content = @Content(schema = @Schema(implementation = VisitedCountryResponseDto.class))),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/my-countries")
    public ResponseEntity<ListResponse<VisitedCountryResponseDto>> getVisitedCountries(
        @AuthenticationPrincipal CustomUserDetails userDetails) {
        Long userId = userDetails.getUserId();
        List<VisitedCountryResponseDto> visitedCountries = visitedCountryService.getVisitedCountries(userId)
                .stream()
                .map(VisitedCountryResponseDto::from)
                .toList();
        return ResponseEntity.ok(new ListResponse<>(visitedCountries));
    }


    // 공통 마스터 데이터 조회이므로 JWT 인증 불필요 - 모든 사용자가 동일한 국가 목록 검색
    @Operation(summary = "나라 검색", description = "한글로 나라 이름을 검색합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "검색 성공",
            content = @Content(schema = @Schema(implementation = CountrySearchResponseDto.class))),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @GetMapping("/search")
    public ResponseEntity<ListResponse<CountrySearchResponseDto>> searchCountries(
        @Parameter(description = "검색할 나라 이름(한글)", example = "한국")
        @RequestParam String keyword
        
    ) {
        try {
            // Controller에서 빈 키워드일 때 빈 리스트 반환하지 않고, service에서 전체 국가 반환하도록 위임
            List<Country> countries = countryService.searchCountriesByName(keyword);
            List<CountrySearchResponseDto> result = countries.stream()
                .map(c -> new CountrySearchResponseDto(c.getCountryCode(), c.getCountryName(), c.getEmoji()))
                .toList();
            System.out.println("국가 검색 결과: " + result.size() + "개 국가 발견");
            return ResponseEntity.ok(new ListResponse<>(result));
        } catch (Exception e) {
            System.out.println("국가 검색 중 오류 발생: " + e.getMessage());
            return ResponseEntity.ok(new ListResponse<>(List.of()));
        }
    }


    

    @Operation(summary = "방문 국가 저장", description = "JWT 토큰으로 인증된 사용자의 방문 국가를 저장합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "저장 성공", content = @Content()),
        @ApiResponse(responseCode = "400", description = "잘못된 요청", content = @Content()),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @PostMapping("/register")
    public ResponseEntity<Void> registerVisitedCountry(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "방문 국가 등록 요청 DTO (countryCode, emotionId)",
            required = true,
            content = @Content(schema = @Schema(implementation = RegisterVisitedCountryRequestDto.class))
        )
        @RequestBody RegisterVisitedCountryRequestDto requestDto
    ) {
        try {
            Long userId = userDetails.getUserId();
            visitedCountryService.registerVisitedCountry(userId, requestDto.getCountryCode(), requestDto.getEmotionId());
            return ResponseEntity.ok().build();
        } catch (IllegalArgumentException e) {
            System.out.println("방문 국가 등록 실패: " + e.getMessage());
            if (e.getMessage().contains("해당 국가가 존재하지 않습니다")) {
                return ResponseEntity.badRequest().build();
            } else if (e.getMessage().contains("해당 사용자가 존재하지 않습니다")) {
                return ResponseEntity.badRequest().build();
            } else if (e.getMessage().contains("감정을 찾을 수 없습니다")) {
                return ResponseEntity.badRequest().build();
            } else if (e.getMessage().contains("국가를 선택해야 합니다")) {
                return ResponseEntity.badRequest().build();
            }
            return ResponseEntity.badRequest().build();
        } catch (Exception e) {
            System.out.println("방문 국가 등록 중 예상치 못한 오류: " + e.getMessage());
            return ResponseEntity.internalServerError().build();
        }
    }

    @Operation(summary = "방문 국가 삭제", description = "JWT 토큰으로 인증된 사용자의 방문 국가를 삭제합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "삭제 성공", content = @Content()),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @DeleteMapping("/delete")
    public ResponseEntity<Void> deleteVisitedCountry(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @Parameter(description = "삭제할 국가 코드", example = "KR")
        @RequestParam String countryCode
    ) {
        Long userId = userDetails.getUserId();
        visitedCountryService.deleteVisitedCountry(userId, countryCode);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "방문 국가 감정 색상 수정", description = "JWT 토큰으로 인증된 사용자의 방문 국가 감정 색상을 수정합니다.")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "수정 성공", content = @Content()),
        @ApiResponse(responseCode = "401", description = "인증 실패", content = @Content()),
        @ApiResponse(responseCode = "500", description = "서버 오류", content = @Content())
    })
    @PatchMapping("/color")
    public ResponseEntity<Void> updateVisitedCountryColor(
        @AuthenticationPrincipal CustomUserDetails userDetails,
        @io.swagger.v3.oas.annotations.parameters.RequestBody(
            description = "방문 국가 색상 수정 요청 DTO (countryCode, newColor)",
            required = true,
            content = @Content(schema = @Schema(implementation = UpdateVisitedCountryColorRequestDto.class))
        )
        @RequestBody UpdateVisitedCountryColorRequestDto requestDto
    ) {
        Long userId = userDetails.getUserId();
        visitedCountryService.updateVisitedCountryColor(userId, requestDto.getCountryCode(), requestDto.getNewColor());
        return ResponseEntity.ok().build();
    }

} 