package lv.phonenumbervalidator.startup;

import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class CountryCodeCrawler {

    private static final String URL = "https://en.wikipedia.org/wiki/List_of_country_calling_codes";

    private final RestTemplate countryCodeRestTemplate;

    @EventListener
    public void loadCountryCodes(ApplicationReadyEvent e) {
//        throw new UnsupportedOperationException();
    }

}
