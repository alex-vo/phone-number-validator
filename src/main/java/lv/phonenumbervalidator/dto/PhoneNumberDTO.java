package lv.phonenumbervalidator.dto;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import lv.phonenumbervalidator.validation.PhoneNumber;

import javax.validation.constraints.NotBlank;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class PhoneNumberDTO {
    @NotBlank(message = "phone number cannot be blank")
    @PhoneNumber
    String phoneNumber;
}
