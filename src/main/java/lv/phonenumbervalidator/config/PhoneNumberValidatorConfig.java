package lv.phonenumbervalidator.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class PhoneNumberValidatorConfig {

    @Bean
    public RestTemplate countryCodeRestTemplate() {
        return new RestTemplate();
    }

}
