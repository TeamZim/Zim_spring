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
        
        // 대표 사진 설정: 직접 설정된 경우 사용, 없으면 첫 번째 다이어리의 대표 사진 사용
        String representativeImageUrl = trip.getRepresentativeImageUrl();
        if ((representativeImageUrl == null || representativeImageUrl.trim().isEmpty()) && 
            !trip.getDiaries().isEmpty()) {
            // 첫 번째 다이어리의 대표 사진 찾기 (createdAt 기준으로 정렬)
            representativeImageUrl = trip.getDiaries().stream()
                    .sorted((d1, d2) -> d1.getCreatedAt().compareTo(d2.getCreatedAt()))
                    .findFirst()
                    .flatMap(diary -> diary.getDiaryImages().stream()
                            .filter(image -> image.isRepresentative())
                            .findFirst()
                            .map(image -> image.getImageUrl()))
                    .orElse(null);
        }
        dto.setRepresentativeImageUrl(representativeImageUrl);
        
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