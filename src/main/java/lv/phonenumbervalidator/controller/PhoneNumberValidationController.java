package lv.phonenumbervalidator.controller;

import lombok.RequiredArgsConstructor;
import lv.phonenumbervalidator.infrastructure.NotFoundException;
import lv.phonenumbervalidator.repository.CountryCodeRepository;
import org.springframework.context.support.DefaultMessageSourceResolvable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class PhoneNumberValidationController {

    private final CountryCodeRepository countryCodeRepository;

    @GetMapping("api/v1/phone-number/validate")
    public Map<String, String> validatePhoneNumber(
            @RequestParam("rawNumber")
            @NotBlank(message = "phone number cannot be blank")
            @Pattern(
                    regexp = "^(\\+|00)(\\d| )+",
                    message = "phone number should start with + or 00 followed by 1 or more digits"
            ) String phoneNumber
    ) {
        List<String> country = countryCodeRepository.findCountryCodeForPhoneNumber(phoneNumber
                .replaceAll("\\s+", "")
                .replaceFirst("^(00)", "+"));
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
        return new ResponseEntity<>(Map.of("message", e.getMessage()), HttpStatus.NOT_FOUND);
    }

}
