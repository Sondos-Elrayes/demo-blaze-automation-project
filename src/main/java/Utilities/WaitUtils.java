package Utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class WaitUtils {

    public static WebDriverWait generalWait(WebDriver driver) {
        return new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public static WebElement waitForElementToBeVisible(WebDriver driver, By locator) {
        if (driver == null) {
            System.err.println("WebDriver instance is null.");
            return null;
        }

        if (locator == null) {
            System.err.println("Locator is null.");
            return null;
        }

        try {
            return generalWait(driver).until(ExpectedConditions.visibilityOfElementLocated(locator));
        } catch (TimeoutException e) {
            System.err.println("Timeout: Element not visible after waiting. Locator: " + locator);
        } catch (NoSuchElementException e) {
            System.err.println("NoSuchElementException: Element not found. Locator: " + locator);
        } catch (Exception e) {
            System.err.println("Unexpected error while waiting for element: " + e.getMessage());
        }

        return null;
    }


    public static void waitForProductToDisappear(String productName,WebDriver driver) {
        By locator = By.xpath("//tr[@class='success']/td[2][text()='" + productName + "']");
        generalWait(driver).until(ExpectedConditions.invisibilityOfElementLocated(locator));
    }









}
