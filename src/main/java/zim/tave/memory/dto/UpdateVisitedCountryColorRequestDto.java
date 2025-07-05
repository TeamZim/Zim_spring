package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "방문한 국가 색상 업데이트 요청 DTO")
public class UpdateVisitedCountryColorRequestDto {
    
    @Schema(description = "국가 코드", example = "KR", required = true)
    private String countryCode;
    
    @Schema(description = "새로운 색상", example = "#FF6B6B", required = true)
    private String newColor;
} 