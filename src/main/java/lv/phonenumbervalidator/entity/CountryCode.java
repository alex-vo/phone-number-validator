package lv.phonenumbervalidator.entity;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "country_code")
@FieldDefaults(level = AccessLevel.PRIVATE)
@Data
public class CountryCode {

    @Id
    @GeneratedValue
    Long id;

    String country;

    String code;
}
