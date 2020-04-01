package lv.phonenumbervalidator.info;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

@Data
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CountryCode {
    String countryISO;
    String code;
}
