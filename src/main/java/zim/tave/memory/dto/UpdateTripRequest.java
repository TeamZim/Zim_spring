package zim.tave.memory.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class UpdateTripRequest {
    
    private String tripName;
    private String description;
    private Long themeId;
    private String representativeImageUrl;
    private LocalDate startDate;
    private LocalDate endDate;
} 