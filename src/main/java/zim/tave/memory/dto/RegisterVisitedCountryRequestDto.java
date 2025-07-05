package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "방문한 국가 등록 요청 DTO")
public class RegisterVisitedCountryRequestDto {
    
    @Schema(description = "국가 코드", example = "KR", required = true)
    private String countryCode;
    
    @Schema(description = "감정 ID (선택사항)", example = "1")
    private Long emotionId;
} 