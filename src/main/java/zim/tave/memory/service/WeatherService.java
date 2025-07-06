package zim.tave.memory.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Weather;
import zim.tave.memory.dto.WeatherResponseDto;
import zim.tave.memory.repository.WeatherRepository;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class WeatherService {

    private final WeatherRepository weatherRepository;

    public List<WeatherResponseDto> getAllWeathers() {
        List<Weather> weathers = weatherRepository.findAllByOrderById();
        return weathers.stream()
                .map(WeatherResponseDto::from)
                .collect(Collectors.toList());
    }

    public WeatherResponseDto getWeatherById(Long weatherId) {
        Weather weather = weatherRepository.findById(weatherId)
                .orElseThrow(() -> new IllegalArgumentException("날씨를 찾을 수 없습니다: " + weatherId));
        return WeatherResponseDto.from(weather);
    }
} 