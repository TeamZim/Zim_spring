package zim.tave.memory.dto;

import lombok.Getter;
import lombok.Setter;
import zim.tave.memory.domain.DiaryImage;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
public class CreateDiaryRequest {
    
    private Long userId;
    private Long tripId;
    private String countryCode;
    private String city;
    private LocalDateTime dateTime;
    private String content;
    
    // 이미지 정보 (전면/후면 카메라)
    private List<DiaryImageInfo> images;
    
    // 선택적 필드들
    private String detailedLocation;
    private String audioUrl;
    private Long emotionId;
    private Long weatherId;
    
    @Getter
    @Setter
    public static class DiaryImageInfo {
        private String imageUrl;
        private DiaryImage.CameraType cameraType;
        private boolean isRepresentative;
    }
} 