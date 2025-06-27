package zim.tave.memory.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import zim.tave.memory.domain.Country;
import zim.tave.memory.repository.CountryRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@SpringBootTest
public class CountryServiceTest {

    @Autowired
    private CountryService countryService;

    @Autowired
    private CountryRepository countryRepository;

    @BeforeEach
    void clearDB() {
        countryRepository.findAll().forEach(c -> countryRepository.delete(c));
    }

    @Test
    void init_빈_DB에_정상작동_확인() {
        // when
        countryService.init();

        // then
        List<Country> result = countryRepository.findAll();
        assertThat(result).isNotEmpty();
        assertThat(result).anyMatch(c -> c.getCountryCode().equals("KR"));
        assertThat(result).anyMatch(c -> c.getCountryCode().equals("US"));
    }

    @Test
    void init_중복저장_방지_확인() {
        // given
        countryService.init();
        int firstCount = countryRepository.findAll().size();

        // when
        countryService.init(); // 두 번째 실행
        int secondCount = countryRepository.findAll().size();

        // then
        assertThat(secondCount).isEqualTo(firstCount); // 중복 저장 안 됐는지 확인
    }

    @Test
    void searchCountryByKeyword() {
        // given
        countryRepository.save(new Country("KR", "대한민국", "🇰🇷"));
        countryRepository.save(new Country("TW", "대만", "🇹🇼"));
        countryRepository.save(new Country("US", "미국", "🇺🇸"));

        // when
        List<Country> result = countryService.searchCountriesByName("대");

        // then
        assertThat(result).extracting("countryName")
                .contains("대한민국", "대만")
                .doesNotContain("미국");
    }
}
