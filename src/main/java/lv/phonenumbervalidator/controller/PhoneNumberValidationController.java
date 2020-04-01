package lv.phonenumbervalidator.controller;

import lv.phonenumbervalidator.dto.PhoneNumberDTO;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
public class PhoneNumberValidationController {

    @PostMapping("validatePhoneNumber")
    public void validatePhoneNumber(@RequestBody @Valid PhoneNumberDTO phoneNumberDTO) {
    }

}
