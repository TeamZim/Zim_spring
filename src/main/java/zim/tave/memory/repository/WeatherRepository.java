package zim.tave.memory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zim.tave.memory.domain.Weather;

import java.util.List;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    
    List<Weather> findAllByOrderById();
    
    Weather findByName(String name);
} 