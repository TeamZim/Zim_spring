package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import zim.tave.memory.domain.TripTheme;

@Getter
@Setter
@Schema(description = "여행 테마 응답")
public class TripThemeResponseDto {
    
    @Schema(description = "테마 ID", example = "1")
    private Long id;
    
    @Schema(description = "테마 이름", example = "기본")
    private String themeName;
    
    @Schema(description = "테마 선택 시 보여줄 샘플 이미지 URL", example = "https://images.example.com/themes/basic/sample.jpg")
    private String sampleImageUrl;
    
    @Schema(description = "실제 카드에 들어갈 이미지 URL", example = "https://images.example.com/themes/basic/card.jpg")
    private String cardImageUrl;
    
    public static TripThemeResponseDto from(TripTheme tripTheme) {
        TripThemeResponseDto dto = new TripThemeResponseDto();
        dto.setId(tripTheme.getId());
        dto.setThemeName(tripTheme.getThemeName());
        dto.setSampleImageUrl(tripTheme.getSampleImageUrl());
        dto.setCardImageUrl(tripTheme.getCardImageUrl());
        return dto;
    }
} 