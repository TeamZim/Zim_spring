package zim.tave.memory.dto;

import lombok.Getter;
import lombok.Setter;
import zim.tave.memory.domain.Trip;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
public class TripResponseDto {
    
    private Long id;
    private String tripName;
    private String description;
    private LocalDateTime createdAt;
    private LocalDate startDate;
    private LocalDate endDate;
    private Boolean isDeleted;
    
    // 간단한 사용자 정보
    private Long userId;
    private String userKakaoId;
    private String userKoreanName;
    
    // 간단한 테마 정보
    private Long themeId;
    private String themeName;
    
    // 다이어리 개수
    private int diaryCount;
    
    public static TripResponseDto from(Trip trip) {
        TripResponseDto dto = new TripResponseDto();
        dto.setId(trip.getId());
        dto.setTripName(trip.getTripName());
        dto.setDescription(trip.getDescription());
        dto.setCreatedAt(trip.getCreatedAt());
        dto.setStartDate(trip.getStartDate());
        dto.setEndDate(trip.getEndDate());
        dto.setIsDeleted(trip.getIsDeleted());
        
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
        }
        
        // 다이어리 개수
        dto.setDiaryCount(trip.getDiaries().size());
        
        return dto;
    }
} 