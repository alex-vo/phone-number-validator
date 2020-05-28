package lv.phonenumbervalidator.startup;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import lv.phonenumbervalidator.entity.CountryCode;
import lv.phonenumbervalidator.repository.CountryCodeRepository;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
@Slf4j
@RequiredArgsConstructor
public class CountryCodeCrawler {

    static final String COUNTRY_CODE_TABLE_HEADER_ID_SELECTOR = "Alphabetical_listing_by_country_or_region";

    private final Connection countryCodeUrlConnection;
    private final CountryCodeRepository countryCodeRepository;

    @EventListener
    public void loadCountryCodes(ApplicationReadyEvent e) {
        try {
            Element countryCodeTable = countryCodeUrlConnection.get()
                    .getElementById(COUNTRY_CODE_TABLE_HEADER_ID_SELECTOR)
                    .parent()
                    .nextElementSibling();
            if (countryCodeTable == null || !StringUtils.equalsIgnoreCase("table", countryCodeTable.nodeName())) {
                throw new RuntimeException("Failed to find HTML table containing country codes");
            }

            List<CountryCode> countryCodes = parseCountryCodes(countryCodeTable);

            countryCodeRepository.saveAll(countryCodes);
        } catch (Exception ex) {
            log.error("Failed to read country code data from Wikipedia", ex);
        }
    }

    private List<CountryCode> parseCountryCodes(Element countryCodeTable) {
        return countryCodeTable.select("tr")
                .stream()
                .map(tr -> {
                    tr.select("sup").remove();
                    return tr.select("td");
                })
                .filter(tds -> !tds.isEmpty())
                .map(this::getCountryCodeFromTds)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
    }

    private CountryCode getCountryCodeFromTds(Elements tds) {
        try {
            String country = tds.get(0).text();
            String code = tds.get(1).select("a").text().replace(" ", "");
            if (StringUtils.isAnyBlank(country, code)) {
                throw new RuntimeException(String.format("Unexpected empty value: country=%s, code=%s. Raw data: %s",
                        country, code, tds));
            }

            CountryCode countryCode = new CountryCode();
            countryCode.setCountry(country);
            countryCode.setCode(code);
            return countryCode;
        } catch (Throwable throwableWhileParsing) {
            log.error(String.format("Failed to parse %s", tds), throwableWhileParsing);
            return null;
        }
    }

}
