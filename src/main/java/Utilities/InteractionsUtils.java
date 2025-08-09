package Utilities;

import io.restassured.RestAssured;
import io.restassured.response.Response;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.net.URI;
import java.net.URL;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;


public class InteractionsUtils {

    public static void clickOnElement(WebDriver driver, By locator) {
        // Wait for the element to be clickable
        new WebDriverWait(driver, Duration.ofSeconds(15))
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


    // Scrolling to element
    public static void scrollToElement(WebDriver driver, By locator) {

        ((JavascriptExecutor) driver)
                .executeScript("arguments[0].scrollIntoView(true);"
                        , findWebElement(driver, locator));
    }

    //checkBrokenLinksOrImages
    public static List<String> checkIfLinksAreWorking(List<String> links, String baseURL) {
        List<String> notWorkingLinks = new ArrayList<>();

        for (String link : links) {
            if (link == null || link.isEmpty()) {
                LogsUtils.warn("Empty or null link found.");
                continue;
            }

            if (!link.startsWith("http")) {
                link = baseURL + link;
            }

            try {
                URL url = new URI(link).toURL();
                Response response = RestAssured.given().get(url);
                int statusCode = response.getStatusCode();

                if (statusCode == 200) {
                    LogsUtils.info("Working: " + link + " [Status: " + statusCode + "]");
                } else {
                    LogsUtils.error("Not working: " + link + " [Status: " + statusCode + "]");
                    notWorkingLinks.add(link + " [Status: " + statusCode + "]");
                }

            } catch (Exception e) {
                LogsUtils.error("Exception while checking link: " + link + " - " + e.getMessage());
                notWorkingLinks.add(link + " [Exception: " + e.getMessage() + "]");
            }
        }
        return notWorkingLinks;
    }


    public static boolean isElementDisplayed(WebDriver driver, By locator) {
        try {
            WebElement element = driver.findElement(locator);
            return element.isDisplayed();
        } catch (Exception e) {
            LogsUtils.error("Element not found or not displayed: " + e);
            return false;
        }
    }

    //TODO
    public static List<String> checkIfLinksAreWorking(List<WebElement> webElements, String baseUrl, String photo) {
        List<String> notWorkingImages = new ArrayList<>();
        return notWorkingImages;
    }

    public static boolean isAlertPresent(WebDriver driver) {
        try {
            new WebDriverWait(driver, Duration.ofSeconds(5))
                    .until(ExpectedConditions.alertIsPresent());
            return true;
        } catch (Exception e) {
            LogsUtils.error("No alert present: " + e.getMessage());
            return false;
        }
    }
}
