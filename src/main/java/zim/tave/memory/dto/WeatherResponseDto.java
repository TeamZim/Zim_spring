package zim.tave.memory.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.Setter;
import zim.tave.memory.domain.Weather;

@Getter
@Setter
@Schema(description = "날씨 응답 DTO")
public class WeatherResponseDto {
    
    @Schema(description = "날씨 ID", example = "1")
    private Long id;
    
    @Schema(description = "날씨 이름", example = "맑음")
    private String name;
    
    @Schema(description = "날씨 아이콘 URL", example = "/images/weather/sunny.png")
    private String iconUrl;
    
    public static WeatherResponseDto from(Weather weather) {
        WeatherResponseDto dto = new WeatherResponseDto();
        dto.setId(weather.getId());
        dto.setName(weather.getName());
        dto.setIconUrl(weather.getIconUrl());
        return dto;
    }
} 