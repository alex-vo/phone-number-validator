package lv.phonenumbervalidator.dto;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

@Getter
@Setter
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PhoneNumberDTO {
    @NotBlank(message = "phone number cannot be blank")
    @Pattern(regexp = "^(\\+|00)(\\d| )+", message = "phone number should start with + or 00 followed by 1 or more digits")
    String phoneNumber;
}
