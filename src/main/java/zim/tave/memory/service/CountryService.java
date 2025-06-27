package zim.tave.memory.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import zim.tave.memory.domain.Country;
import zim.tave.memory.repository.CountryRepository;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class CountryService {

    private final CountryRepository countryRepository;

    public List<Country> searchCountriesByName(String keyword) {
        return countryRepository.findByNameContaining(keyword);
    }

    public Country findByCode(String countryCode) {
        return countryRepository.findByCode(countryCode);
    }

    public void saveCountry(Country country) {
        countryRepository.save(country);
    }
}
