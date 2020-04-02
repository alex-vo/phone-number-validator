package lv.phonenumbervalidator.controller;

import lombok.RequiredArgsConstructor;
import lv.phonenumbervalidator.dto.PhoneNumberDTO;
import lv.phonenumbervalidator.infrastructure.NotFoundException;
import lv.phonenumbervalidator.repository.CountryCodeRepository;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PhoneNumberValidationController {

    private final CountryCodeRepository countryCodeRepository;

    @PostMapping("api/v1/validatePhoneNumber")
    public Map<String, String> validatePhoneNumber(@RequestBody @Valid PhoneNumberDTO phoneNumberDTO) {
        String phoneNumber = phoneNumberDTO.getPhoneNumber()
                .replaceAll("\\s+", "")
                .replaceFirst("^(00)", "+");
        List<String> country = countryCodeRepository.findCountryCodeForPhoneNumber(phoneNumber);
        if (CollectionUtils.isEmpty(country)) {
            throw new NotFoundException("Country code not recognized");
        }

        return Map.of("country", String.join(" or ", country));
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    public ResponseEntity<Map<String, String>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        List<FieldError> fieldErrors = e.getBindingResult().getFieldErrors();
        return new ResponseEntity<>(Map.of("message", fieldErrors.stream()
                .map(DefaultMessageSourceResolvable::getDefaultMessage)
                .collect(Collectors.joining(", "))), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler({NotFoundException.class})
    public ResponseEntity<Map<String, String>> handleNotFoundException(NotFoundException e) {
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.BAD_REQUEST);
    }

}
