package zim.tave.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import zim.tave.memory.domain.VisitedCountry;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class VisitedCountryResponseDto {
    private Long visitedCountryId;
    private String countryCode;
    private String countryName;
    private String emoji;
    private String emotionName;
    private String color;
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