package zim.tave.memory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTripRequest {
    
    private String tripName;
    private String description;
    private Long themeId;
    private Long userId;
} 