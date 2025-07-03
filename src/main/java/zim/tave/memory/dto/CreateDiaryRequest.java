package zim.tave.memory.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import zim.tave.memory.domain.DiaryImage;
import com.fasterxml.jackson.annotation.JsonFormat;
import zim.tave.memory.domain.DiaryImage.CameraType;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Schema(description = "일기 생성 요청")
public class CreateDiaryRequest {
    
    @Schema(description = "사용자 ID", example = "1", required = true)
    private Long userId;
    
    @Schema(description = "여행 ID", example = "1", required = true)
    private Long tripId;
    
    @Schema(description = "국가 코드", example = "KR", required = true)
    private String countryCode;
    
    @Schema(description = "도시명", example = "제주시", required = true)
    private String city;
    
    @Schema(description = "일기 작성 날짜 및 시간", example = "2023-12-25T14:30:00", required = true)
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime dateTime;
    
    @Schema(description = "일기 내용", example = "오늘은 제주도에서 멋진 하루를 보냈다.", required = true)
    private String content;
    
    @Schema(description = "이미지 정보 목록 (정면/후면 카메라 각 1장씩, 총 2장 필요)", required = true)
    private List<DiaryImageInfo> images;
    
    // 선택적 필드들
    @Schema(description = "상세 위치", example = "한라산 정상")
    private String detailedLocation;
    
    @Schema(description = "오디오 URL", example = "https://audio-bucket.s3.amazonaws.com/audio.mp3")
    private String audioUrl;
    
    @Schema(description = "감정 ID", example = "1")
    private Long emotionId;
    
    @Schema(description = "날씨 ID", example = "1")
    private Long weatherId;
    
    @Getter
    @Setter
    @Schema(description = "일기 이미지 정보")
    public static class DiaryImageInfo {
        
        @Schema(description = "이미지 URL", example = "https://image-bucket.s3.amazonaws.com/image.jpg", required = true)
        private String imageUrl;
        
        @Schema(description = "카메라 타입", example = "FRONT", required = true, allowableValues = {"FRONT", "BACK"})
        private CameraType cameraType;
        
        @Schema(description = "대표 이미지 여부 (정확히 1개만 true여야 함)", example = "true", required = true)
        @JsonProperty("isRepresentative")
        private boolean representative;

        public DiaryImageInfo() {}

        public DiaryImageInfo(String imageUrl, CameraType cameraType, boolean representative) {
            this.imageUrl = imageUrl;
            this.cameraType = cameraType;
            this.representative = representative;
        }
    }
} 