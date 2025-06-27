package zim.tave.memory.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import zim.tave.memory.domain.Setting;

public interface SettingRepository extends JpaRepository<Setting, Long> {
}