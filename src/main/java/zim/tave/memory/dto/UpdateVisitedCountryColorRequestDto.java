package zim.tave.memory.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class UpdateVisitedCountryColorRequestDto {
    private String countryCode;
    private String newColor;
} 