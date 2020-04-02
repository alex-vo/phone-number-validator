package lv.phonenumbervalidator.repository;

import lv.phonenumbervalidator.entity.CountryCode;
import org.junit.Before;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

@DataJpaTest
public class CountryCodeRepositoryTest {

    @Autowired
    CountryCodeRepository countryCodeRepository;

    @Before
    public void setup() {
        countryCodeRepository.saveAll(List.of(
                prepareCountryCode("Usa", "+1"),
                prepareCountryCode("Canada", "+1"),
                prepareCountryCode("Bahamas", "+1242")
        ));
    }

    @Test
    public void shouldFindCountryWithLongestMatchingCountryCode() {
        List<String> result = countryCodeRepository.findCountryCodeForPhoneNumber("+12420");

        assertThat(result, contains("Bahamas"));
    }

    @Test
    public void shouldFindCountryWithLongestMatchingCountryCodeButNotEntirelyMatching() {
        List<String> result = countryCodeRepository.findCountryCodeForPhoneNumber("+1242");

        assertThat(result, contains("Usa", "Canada"));
    }

    @Test
    public void shouldFailToFindCountryByNonExistingCode() {
        List<String> result = countryCodeRepository.findCountryCodeForPhoneNumber("+82828");

        assertThat(result, is(empty()));
    }

    private CountryCode prepareCountryCode(String country, String code) {
        CountryCode countryCode = new CountryCode();
        countryCode.setCountry(country);
        countryCode.setCode(code);
        return countryCode;
    }

}
