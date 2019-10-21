package com.claire.qanda;

import io.github.artsok.RepeatedIfExceptionsTest;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class QuestionTest {

    @Test
    void should_not_accept_empty_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Question("", Question.QType.OPEN)
        );
    }

    @Test
    void should_not_accept_null_statement() {
        assertThrows(
                NullPointerException.class,
                () -> new Question(null, Question.QType.OPEN)
        );
    }

    @Test
    void should_not_accept_blank_statement() {
        assertThrows(
                IllegalArgumentException.class,
                () -> new Question("   \t\n", Question.QType.OPEN)
        );
    }

    @Test
    void should_accept_non_blank_statement() {
        assertDoesNotThrow(
                () -> new Question("abc", Question.QType.OPEN)
        );
    }

    @Test
    void should_require_question_type() {
        assertThrows(
                NullPointerException.class,
                () -> new Question("abc", null)
        );
    }


    @RepeatedIfExceptionsTest(repeats = 2)
    void google_test() throws InterruptedException {
        WebDriver driver = preparedWebDriver();
        WebDriver frame = prepareInputFrame(driver);
        WebElement emailInput = input(frame, "//input[@type='email']");
        WebElement submitInput = input(frame, "//input[@type='submit']");
        emailInput.sendKeys("abcdefg");
        Thread.sleep(1000);
        submitInput.click();
    }

    private WebElement input(WebDriver frame, String s) {
        return frame.findElements(By.xpath(s)).iterator().next();
    }

    private WebDriver prepareInputFrame(WebDriver driver) {
        List<WebElement> iframes = driver.findElements(By.tagName("iframe"));
        int iframeCount = iframes.size();
        return driver.switchTo().frame(iframes.get(iframeCount - 1));
    }

    private WebDriver preparedWebDriver() {
        System.setProperty("webdriver.chrome.driver", "/home/moran/dev/q-and-a/qanda/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
        driver.get("https://onedrive.live.com/about/en-us/signin/");
        return driver;
    }

}

