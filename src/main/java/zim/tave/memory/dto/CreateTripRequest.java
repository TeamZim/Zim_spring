package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Schema(description = "여행 생성 요청")
public class CreateTripRequest {
    
    @Schema(description = "여행 이름", example = "제주도 여행", maxLength = 14, required = true)
    private String tripName;
    
    @Schema(description = "여행 설명", example = "가족과 함께하는 제주도 여행", maxLength = 56)
    private String description;
    
    @Schema(description = "여행 테마 ID", example = "1", required = true)
    private Long themeId;
    
    @Schema(description = "사용자 ID", example = "1", required = true)
    private Long userId;
} 