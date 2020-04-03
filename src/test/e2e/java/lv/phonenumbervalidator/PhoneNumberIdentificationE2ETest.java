package lv.phonenumbervalidator;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.test.context.junit4.SpringRunner;

import java.net.MalformedURLException;
import java.net.URL;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class PhoneNumberIdentificationE2ETest {

    @LocalServerPort
    int port;

    @Test
    public void shouldIdentifyLatvianNumber() throws MalformedURLException {
        WebDriver webDriver = new ChromeDriver();

        webDriver.navigate().to(new URL("http://localhost:" + port + "/index.html"));

        WebDriverWait wait = new WebDriverWait(webDriver, 10);
        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.tagName("input"), 0));

        WebElement input = webDriver.findElement(By.cssSelector("#phone-number-input"));
        input.sendKeys("+371 21111111");
        input.sendKeys(Keys.ENTER);

        wait.until(ExpectedConditions.numberOfElementsToBeMoreThan(By.cssSelector("#result"), 0));
        WebElement result = webDriver.findElement(By.cssSelector("#result"));
        wait.until(ExpectedConditions.textToBePresentInElement(result, "Latvia"));

        webDriver.quit();
    }

}
