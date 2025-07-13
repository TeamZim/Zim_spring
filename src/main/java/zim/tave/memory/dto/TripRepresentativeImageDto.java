package zim.tave.memory.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class TripRepresentativeImageDto {
    
    private Long diaryId;
    private Long imageId;
    private String imageUrl;
    private String countryCode;
    private String countryName;
    private String city;
    private String content;
    
    public TripRepresentativeImageDto() {}
    
    public TripRepresentativeImageDto(Long diaryId, Long imageId, String imageUrl, String countryCode, 
                                    String countryName, String city, String content) {
        this.diaryId = diaryId;
        this.imageId = imageId;
        this.imageUrl = imageUrl;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.city = city;
        this.content = content;
    }
} 