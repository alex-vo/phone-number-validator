package lv.phonenumbervalidator.controller;

import lombok.RequiredArgsConstructor;
import lv.phonenumbervalidator.dto.PhoneNumberDTO;
import lv.phonenumbervalidator.repository.CountryCodeRepository;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class PhoneNumberValidationController {

    private final CountryCodeRepository countryCodeRepository;

    @PostMapping("api/v1/validatePhoneNumber")
    public Map<String, String> validatePhoneNumber(@RequestBody @Valid PhoneNumberDTO phoneNumberDTO) {
        List<String> country = countryCodeRepository.findCountryCodeForPhoneNumber(phoneNumberDTO.getPhoneNumber());
        if (CollectionUtils.isEmpty(country)) {
            throw new RuntimeException("Country code not recognized");
        }

        return Map.of("country", String.join(" or ", country));
    }

}
