package lv.phonenumbervalidator.repository;

import lv.phonenumbervalidator.entity.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {
}
