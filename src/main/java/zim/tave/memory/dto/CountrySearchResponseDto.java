package zim.tave.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class CountrySearchResponseDto {
    private String countryCode;
    private String countryName;
    private String emoji;
} 