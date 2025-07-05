package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Schema(description = "국가 검색 응답 DTO")
public class CountrySearchResponseDto {
    
    @Schema(description = "국가 코드", example = "KR")
    private String countryCode;
    
    @Schema(description = "국가명", example = "대한민국")
    private String countryName;
    
    @Schema(description = "국가 이모지", example = "🇰🇷")
    private String emoji;
} 