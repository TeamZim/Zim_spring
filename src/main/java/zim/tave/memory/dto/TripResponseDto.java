package zim.tave.memory.dto;

import lombok.Getter;
import lombok.Setter;
import zim.tave.memory.domain.Trip;

import java.time.LocalDate;

@Getter
@Setter
public class TripResponseDto {
    
    private Long id;
    private String tripName;
    private String description;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDeleted;
    private String representativeImageUrl; // 여행의 대표 사진 URL
    
    // 간단한 사용자 정보
    private Long userId;
    private String userKakaoId;
    private String userKoreanName;
    
    // 테마 정보
    private Long themeId;
    private String themeName;
    private String themeSampleImageUrl;  // 테마 선택 시 보여줄 샘플 이미지
    private String themeCardImageUrl;    // 실제 카드에 들어갈 이미지
    
    // 다이어리 개수
    private int diaryCount;
    
    public static TripResponseDto from(Trip trip) {
        TripResponseDto dto = new TripResponseDto();
        dto.setId(trip.getId());
        dto.setTripName(trip.getTripName());
        dto.setDescription(trip.getDescription());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setIsDeleted(trip.getIsDeleted());
        dto.setRepresentativeImageUrl(trip.getRepresentativeImageUrl());
        
        // 사용자 정보
        if (trip.getUser() != null) {
            dto.setUserId(trip.getUser().getId());
            dto.setUserKakaoId(trip.getUser().getKakaoId());
            dto.setUserKoreanName(trip.getUser().getKoreanName());
        }
        
        // 테마 정보
        if (trip.getTripTheme() != null) {
            dto.setThemeId(trip.getTripTheme().getId());
            dto.setThemeName(trip.getTripTheme().getThemeName());
            dto.setThemeSampleImageUrl(trip.getTripTheme().getSampleImageUrl());
            dto.setThemeCardImageUrl(trip.getTripTheme().getCardImageUrl());
        }
        
        // 다이어리 개수
        dto.setDiaryCount(trip.getDiaries().size());
        
        return dto;
    }
} 