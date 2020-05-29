package lv.phonenumbervalidator.repository;

import lv.phonenumbervalidator.entity.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {

    @Query(value = "select country " +
            "from ( " +
            "   select cc.country, rank() over (order by length(cc.code) desc) as rnk " +
            "   from country_code cc " +
            "   where :pPhoneNumber like cc.code || '%' and :pPhoneNumber <> cc.code " +
            ") matching_countries " +
            "where matching_countries.rnk = 1", nativeQuery = true)
    List<String> findCountryCodeForPhoneNumber(@Param("pPhoneNumber") String phoneNumber);

}