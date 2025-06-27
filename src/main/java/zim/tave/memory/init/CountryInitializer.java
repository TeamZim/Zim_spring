package zim.tave.memory.init;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import zim.tave.memory.service.CountryService;

@Component
@RequiredArgsConstructor
public class CountryInitializer implements CommandLineRunner {

    private final CountryService countryService;

    @Override
    public void run(String... args) {
        countryService.init();
    }
}
