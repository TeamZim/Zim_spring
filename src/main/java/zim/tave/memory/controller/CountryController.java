package zim.tave.memory.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.service.VisitedCountryService;
import zim.tave.memory.dto.VisitedCountryResponseDto;
import zim.tave.memory.dto.RegisterVisitedCountryRequestDto;
import zim.tave.memory.dto.UpdateVisitedCountryColorRequestDto;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/countries")
public class CountryController {

    private final VisitedCountryService visitedCountryService;

    // 1. 특정 사용자의 방문 국가 전체 조회
    @GetMapping("/{userId}")
    public ResponseEntity<List<VisitedCountryResponseDto>> getVisitedCountries(@PathVariable Long userId) {
        List<VisitedCountryResponseDto> visitedCountries = visitedCountryService.getVisitedCountries(userId)
                .stream()
                .map(VisitedCountryResponseDto::from)
                .toList();
        return ResponseEntity.ok(visitedCountries);
    }

    // 2. 방문 국가 저장 (국가코드 + 감정ID)
    @PostMapping("/{userId}")
    public ResponseEntity<Void> registerVisitedCountry(
            @PathVariable Long userId,
            @RequestBody RegisterVisitedCountryRequestDto requestDto
    ) {
        visitedCountryService.registerVisitedCountry(userId, requestDto.getCountryCode(), requestDto.getEmotionId());
        return ResponseEntity.ok().build();
    }

    // 3. 특정 방문 국가 삭제
    @DeleteMapping("/{userId}")
    public ResponseEntity<Void> deleteVisitedCountry(
            @PathVariable Long userId,
            @RequestParam String countryCode
    ) {
        visitedCountryService.deleteVisitedCountry(userId, countryCode);
        return ResponseEntity.ok().build();
    }

    // 4. 특정 국가 감정 색상 수정
    @PatchMapping("/{userId}/color")
    public ResponseEntity<Void> updateVisitedCountryColor(
            @PathVariable Long userId,
            @RequestBody UpdateVisitedCountryColorRequestDto requestDto
    ) {
        visitedCountryService.updateVisitedCountryColor(userId, requestDto.getCountryCode(), requestDto.getNewColor());
        return ResponseEntity.ok().build();
    }
} 