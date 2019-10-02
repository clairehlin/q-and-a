package com.claire.qanda;

import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.List;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void google_test() throws InterruptedException {
        System.setProperty("webdriver.chrome.driver", "/home/moran/dev/q-and-a/qanda/chromedriver");
        WebDriver driver = new ChromeDriver();
        driver.manage().window().maximize();
//        driver.get("https://www.linkedin.com/login");
        driver.get("https://onedrive.live.com/about/en-us/signin/");
        List<WebElement> elements = driver.findElements(By.xpath("//*")).stream()
                .filter(e -> e.getTagName().equals("input"))
                .collect(toList());
        for (WebElement element : elements) {
            if (element.getTagName().equals("input") && element.getAttribute("type").equals("email")) {
                System.out.println(element.getTagName());
                System.out.println(element.getAttribute("type"));
                System.out.println("text: " + element.getText());
                element.click();
                element.sendKeys("abcdefg@abcdefg.com");
            }
        }
//        System.out.println(elements);
//        WebElement username = driver.findElement(By.id("username"));
//        WebElement password = driver.findElement(By.id("password"));
//        WebElement login = driver.findElement(By.xpath("//button[text()='Sign in']"));
//        username.sendKeys("example@gmail.com");
//        password.sendKeys("password");
//        login.click();
//        String actualUrl = "https://www.linkedin.com/feed/";
//        String expectedUrl = driver.getCurrentUrl();
//        assertEquals(expectedUrl, actualUrl);
    }
}