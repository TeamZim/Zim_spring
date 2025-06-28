package zim.tave.memory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zim.tave.memory.domain.TripTheme;

import java.util.List;

public interface TripThemeRepository extends JpaRepository<TripTheme, Long> {
    
    List<TripTheme> findAllByOrderById();
    
    TripTheme findByThemeName(String themeName);
} 