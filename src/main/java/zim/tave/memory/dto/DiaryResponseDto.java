package zim.tave.memory.dto;

import lombok.Getter;
import lombok.Setter;
import zim.tave.memory.domain.Diary;
import zim.tave.memory.domain.DiaryImage;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class DiaryResponseDto {
    
    private Long id;
    private String countryName;
    private String countryEmoji;
    private String city;
    private String detailedLocation;
    private LocalDateTime dateTime;
    private LocalDateTime createdAt;
    private String content;
    private String audioUrl;
    private String emotionColor;
    private String emotionName;
    private String weather;
    private String weatherIconUrl;
    
    // 간단한 여행 정보
    private Long tripId;
    private String tripName;
    
    // 이미지 정보
    private List<DiaryImageDto> images;
    
    @Getter
    @Setter
    public static class DiaryImageDto {
        private Long id;
        private String imageUrl;
        private String cameraType;
        private Boolean isRepresentative;
        private Integer imageOrder;
        
        public static DiaryImageDto from(DiaryImage image) {
            DiaryImageDto dto = new DiaryImageDto();
            dto.setId(image.getId());
            dto.setImageUrl(image.getImageUrl());
            dto.setCameraType(image.getCameraType().name());
            dto.setIsRepresentative(image.isRepresentative());
            dto.setImageOrder(image.getImageOrder());
            return dto;
        }
    }
    
    public static DiaryResponseDto from(Diary diary) {
        DiaryResponseDto dto = new DiaryResponseDto();
        dto.setId(diary.getId());
        dto.setCity(diary.getCity());
        dto.setDetailedLocation(diary.getDetailedLocation());
        dto.setDateTime(diary.getDateTime());
        dto.setCreatedAt(diary.getCreatedAt());
        dto.setContent(diary.getContent());
        dto.setAudioUrl(diary.getAudioUrl());
        
        // 국가 정보
        if (diary.getCountry() != null) {
            dto.setCountryName(diary.getCountry().getCountryName());
            dto.setCountryEmoji(diary.getCountry().getEmoji());
        }
        
        // 감정 정보
        if (diary.getEmotion() != null) {
            dto.setEmotionColor(diary.getEmotion().getColorCode());
            dto.setEmotionName(diary.getEmotion().getName());
        }
        
        // 날씨 정보
        if (diary.getWeather() != null) {
            dto.setWeather(diary.getWeather().getName());
            dto.setWeatherIconUrl(diary.getWeather().getIconUrl());
        }
        
        // 여행 정보
        if (diary.getTrip() != null) {
            dto.setTripId(diary.getTrip().getId());
            dto.setTripName(diary.getTrip().getTripName());
        }
        
        // 이미지 정보
        List<DiaryImageDto> imageDtos = diary.getDiaryImages().stream()
                .map(DiaryImageDto::from)
                .collect(Collectors.toList());
        dto.setImages(imageDtos);
        
        return dto;
    }
} 