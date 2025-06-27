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
    void setUp() {
        Country korea = new Country();
        korea.setCountryCode("KR");
        korea.setCountryName("대한민국");
        korea.setEmoji("🇰🇷");

        Country taiwan = new Country();
        taiwan.setCountryCode("TW");
        taiwan.setCountryName("대만");
        taiwan.setEmoji("🇹🇼");

        Country usa = new Country();
        usa.setCountryCode("US");
        usa.setCountryName("미국");
        usa.setEmoji("🇺🇸");

        countryRepository.save(korea);
        countryRepository.save(taiwan);
        countryRepository.save(usa);
    }

    @Test
    void searchCountryByKeyword() {
        // when
        List<Country> result = countryService.searchCountriesByName("대");

        // then
        assertThat(result).extracting("countryName")
                .contains("대한민국", "대만")
                .doesNotContain("미국");
    }
}
