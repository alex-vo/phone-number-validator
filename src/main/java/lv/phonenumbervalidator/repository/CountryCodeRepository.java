package lv.phonenumbervalidator.repository;

import lv.phonenumbervalidator.entity.CountryCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface CountryCodeRepository extends JpaRepository<CountryCode, Long> {

    @Query(value = "with max_length_sub_query as ( " +
            "    select max(length(code)) as maxLength " +
            "    from country_code " +
            "    where :pPhoneNumber like code || '%' and :pPhoneNumber <> code " +
            ") " +
            "select cc.country " +
            "from country_code cc " +
            "join max_length_sub_query mlsq on mlsq.maxLength=length(cc.code) " +
            "where :pPhoneNumber like cc.code || '%' and :pPhoneNumber <> cc.code", nativeQuery = true)
    List<String> findCountryCodeForPhoneNumber(@Param("pPhoneNumber") String phoneNumber);

}