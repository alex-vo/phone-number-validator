package lv.phonenumbervalidator.startup;

import lv.phonenumbervalidator.entity.CountryCode;
import lv.phonenumbervalidator.repository.CountryCodeRepository;
import org.hamcrest.MatcherAssert;
import org.hamcrest.Matchers;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.parser.Parser;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.Matchers.hasProperty;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@RunWith(PowerMockRunner.class)
@PrepareForTest({Element.class, CountryCodeCrawler.class})
public class CountryCodeCrawlerTest {

    @Mock
    Connection countryCodeUrlConnection;
    @Mock
    CountryCodeRepository countryCodeRepository;

    CountryCodeCrawler countryCodeCrawler;

    @Before
    public void setUp() {
        countryCodeCrawler = PowerMockito.spy(new CountryCodeCrawler(countryCodeUrlConnection, countryCodeRepository));
    }

    @Test
    public void shouldNotSaveAnyCountryCodeIfDesiredTableNotFound() throws IOException {
        setUpCountryCodeTableElement(null);

        countryCodeCrawler.loadCountryCodes(null);

        verify(countryCodeRepository, never()).saveAll(any());
    }

    @Test
    public void shouldSaveCountryCodes() throws Exception {
        String countryCodeTableHTML = Files.readString(Path.of("src/test/resources/html/country_code_table.txt"));
        Element element = Jsoup.parse(countryCodeTableHTML, "", Parser.xmlParser()).child(0);
        PowerMockito.when(countryCodeCrawler, "parseCountryCodes", element)
                .thenReturn(List.of(
                        prepareCountryCode("Latvia", "+371"),
                        prepareCountryCode("Germany", "+49")
                ));
        setUpCountryCodeTableElement(element);
        ArgumentCaptor<List> countryCodeListCaptor = ArgumentCaptor.forClass(List.class);

        countryCodeCrawler.loadCountryCodes(null);

        verify(countryCodeRepository).saveAll(countryCodeListCaptor.capture());
        List<CountryCode> countryCodes = countryCodeListCaptor.getValue();
        MatcherAssert.assertThat(countryCodes, Matchers.contains(
                allOf(
                        hasProperty("country", is("Latvia")),
                        hasProperty("code", is("+371"))
                ),
                allOf(
                        hasProperty("country", is("Germany")),
                        hasProperty("code", is("+49"))
                )
        ));
    }

    @Test
    public void shouldParseCountryCodeTable() throws IOException {
        String countryCodeTableHTML = Files.readString(Path.of("src/test/resources/html/partially_valid_country_code_table.txt"));
        Element element = Jsoup.parse(countryCodeTableHTML, "", Parser.xmlParser()).child(0);

        List<CountryCode> result = ReflectionTestUtils.invokeMethod(countryCodeCrawler, "parseCountryCodes", element);

        MatcherAssert.assertThat(result, Matchers.contains(
                allOf(
                        hasProperty("country", is("Afghanistan")),
                        hasProperty("code", is("+93"))
                ),
                allOf(
                        hasProperty("country", is("Latvia")),
                        hasProperty("code", is("+371"))
                )
        ));
    }

    private void setUpCountryCodeTableElement(Element countryCodeTableElement) throws IOException {
        Element parentElement = mock(Element.class);
        when(parentElement.nextElementSibling()).thenReturn(countryCodeTableElement);
        Element element = PowerMockito.mock(Element.class);
        PowerMockito.when(element.parent()).thenReturn(parentElement);
        Document document = PowerMockito.mock(Document.class);
        Mockito.when(document.getElementById(CountryCodeCrawler.COUNTRY_CODE_TABLE_HEADER_ID_SELECTOR))
                .thenReturn(element);
        Mockito.when(countryCodeUrlConnection.get())
                .thenReturn(document);
    }

    private CountryCode prepareCountryCode(String country, String code) {
        CountryCode countryCode = new CountryCode();
        countryCode.setCountry(country);
        countryCode.setCode(code);
        return countryCode;
    }

}
