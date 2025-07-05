package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zim.tave.memory.domain.VisitedCountry;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "방문한 국가 응답 DTO")
public class VisitedCountryResponseDto {
    
    @Schema(description = "방문한 국가 ID", example = "1")
    private Long visitedCountryId;
    
    @Schema(description = "국가 코드", example = "KR")
    private String countryCode;
    
    @Schema(description = "국가명", example = "대한민국")
    private String countryName;
    
    @Schema(description = "국가 이모지", example = "🇰🇷")
    private String emoji;
    
    @Schema(description = "감정명", example = "행복")
    private String emotionName;
    
    @Schema(description = "색상", example = "#FF6B6B")
    private String color;
    
    @Schema(description = "사용자 ID", example = "1")
    private Long userId;

    public static VisitedCountryResponseDto from(VisitedCountry visitedCountry) {
        return new VisitedCountryResponseDto(
                visitedCountry.getVisitedCountryId(),
                visitedCountry.getCountry().getCountryCode(),
                visitedCountry.getCountry().getCountryName(),
                visitedCountry.getCountry().getEmoji(),
                visitedCountry.getEmotion().getName(),
                visitedCountry.getColor(),
                visitedCountry.getUser().getId()
        );
    }
} 