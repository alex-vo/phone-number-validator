package lv.phonenumbervalidator.config;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class CountryCodeUrlConnectionConfig {

    @Bean
    public Connection countryCodeUrlConnection(@Value("${country.code.url}") String countryCodeUrl) {
        return Jsoup.connect(countryCodeUrl);
    }

}
