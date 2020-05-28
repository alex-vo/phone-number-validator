package lv.phonenumbervalidator;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.io.File;
import java.io.IOException;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ContextConfiguration(classes = PhoneNumberValidatorTest.TestConfig.class)
@AutoConfigureMockMvc
public class PhoneNumberValidatorTest {

    @TestConfiguration
    public static class TestConfig {
        @Bean
        @Primary
        public Connection countryCodeUrlConnection() throws IOException {
            Connection countryCodeUrlConnection = Mockito.mock(Connection.class);
            when(countryCodeUrlConnection.get()).thenReturn(Jsoup.parse(new File("src/test/resources/html/List_of_country_calling_codes.html"), "UTF-8"));
            return countryCodeUrlConnection;
        }
    }

    @Autowired
    MockMvc mvc;

    @Test
    public void shouldIdentifyLatvianNumber() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                .get("/api/v1/phone-number/validate?rawNumber=+37122222222")
                .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.country").value("Latvia"));
    }

}
