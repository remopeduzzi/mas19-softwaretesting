/*
 * HSR Hochschule fuer Technik Rapperswil
 * Master of Advanced Studies in Software Engineering
 * Module Software Testing
 *
 * Thomas Briner, thomas.briner@gmail.com
 */
package ch.hsr.testing.systemtest.helloworld;

import ch.hsr.testing.systemtest.weekenddiscount.Constants;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;

import java.util.concurrent.TimeUnit;

/**
 * The Class HelloWorldGoogleExample. This class is provided as first example on
 * http://code.google.com/p/selenium/wiki/GettingStarted
 */
public class HelloWorldGoogleExample implements Constants {

    private WebDriver driver;

    @BeforeEach
    public void setup() {
        // Create an instance of the driver of your choice
        //driver = new HtmlUnitDriver();

        System.setProperty("webdriver.chrome.driver", getChromeDriverPath());
        driver = new ChromeDriver();

        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);

    }

    @Test
    public void searchForWordCheese() {
        // And now use this to visit Google
        driver.get("http://www.google.com");

        // Find the text input element by its name
        WebElement element = driver.findElement(By.name("q"));

        // Enter something to search for
        element.sendKeys("Cheese!");

        // Now submit the form. WebDriver will find the form for us from the
        // element
        element.submit();

        // Check the title of the page
        System.out.println("Page title is: " + driver.getTitle());
    }


    @Test
    public void changeLanguageToFrench() {
        // TODO: Implement this
        Assertions.fail("Implement Testcase");

    }
}
