package Utilities;

import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;

public class InteractionsUtils {

    public static void clickOnElement(WebDriver driver, By locator) {
        // Wait for the element to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.elementToBeClickable(locator));
        driver.findElement(locator).click();

    }
    public static void enterText(WebDriver driver, By locator, String text) {
        // Wait for the element to be visible
        new WebDriverWait(driver, Duration.ofSeconds(10))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        driver.findElement(locator).clear();
        driver.findElement(locator).sendKeys(text);
    }
    public static String getText(WebDriver driver, By locator) {
        new WebDriverWait(driver, Duration.ofSeconds(5))
                .until(ExpectedConditions.visibilityOfElementLocated(locator));
        return driver.findElement(locator).getText();
    }

    public static WebElement findWebElement(WebDriver driver, By locator) {

        return driver.findElement(locator);
    }

    public static void HandleAlert(WebDriver driver){
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(15));
        Alert alert = wait.until(ExpectedConditions.alertIsPresent());
        alert.accept();
    }


    // Scrolling to element
    public static void scrollToElement(WebDriver driver, By locator) {

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);"
                , findWebElement(driver,locator));
    }
    //TODO: get timestamp


}
