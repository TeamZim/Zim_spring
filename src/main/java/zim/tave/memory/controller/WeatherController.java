package zim.tave.memory.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import zim.tave.memory.dto.WeatherResponseDto;
import zim.tave.memory.service.WeatherService;

import java.util.List;

@RestController
@RequestMapping("/api/weathers")
@RequiredArgsConstructor
@Tag(name = "Weather", description = "날씨 관련 API")
public class WeatherController {

    private final WeatherService weatherService;

    @GetMapping
    @Operation(summary = "날씨 목록 조회", description = "사용 가능한 모든 날씨 목록을 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "날씨 목록 조회 성공",
                    content = @Content(schema = @Schema(implementation = WeatherResponseDto.class)))
    })
    public List<WeatherResponseDto> getAllWeathers() {
        return weatherService.getAllWeathers();
    }

    @GetMapping("/{weatherId}")
    @Operation(summary = "특정 날씨 조회", description = "ID로 특정 날씨 정보를 조회합니다.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "날씨 조회 성공",
                    content = @Content(schema = @Schema(implementation = WeatherResponseDto.class))),
            @ApiResponse(responseCode = "404", description = "날씨를 찾을 수 없음")
    })
    public WeatherResponseDto getWeatherById(@PathVariable Long weatherId) {
        return weatherService.getWeatherById(weatherId);
    }
} 