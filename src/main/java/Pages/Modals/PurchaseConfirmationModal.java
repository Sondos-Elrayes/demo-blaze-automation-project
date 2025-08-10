package Pages.Modals;

import Pages.HomePage;
import Utilities.DataUtils;
import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class PurchaseConfirmationModal {
    static String dialogMessage;
    private final By dialog = By.cssSelector(".sweet-alert.showSweetAlert.visible");
    private final By title = By.cssSelector(".sweet-alert h2");
    private final By body = By.cssSelector(".sweet-alert p");
    private final By okBtn = By.cssSelector(".sweet-alert button.confirm");
    WebDriver driver;

    public PurchaseConfirmationModal(WebDriver driver) {
        this.driver = driver;
    }

    public PurchaseConfirmationModal waitUntilPurchaseConfirmationOpen() {
        WaitUtils.waitForElementToBeVisible(driver, dialog);
        WaitUtils.waitForElementToBeVisible(driver, title);
        dialogMessage = InteractionsUtils.findWebElement(driver, title).getText().trim();
        LogsUtils.info("Purchase Confirmation dialog opened with message: " + dialogMessage);
        return this;
    }

    //Test case : verify Purchase is done successfully
    //expected: "Thank you for your purchase!" message is displayed
    public String getTitle() {
        WaitUtils.waitForElementToBeVisible(driver, title);
        return InteractionsUtils.findWebElement(driver, title).getText().trim();
    }

    public boolean assertThankYouMessage() {
        String message = dialogMessage;
        if (message.equals("Thank you for your purchase!")) {
            LogsUtils.info("Purchase Confirmation dialog title is correct: " + message);
            return true;
        } else {
            LogsUtils.error("Purchase Confirmation dialog title is incorrect: " + message);
            return false;
        }

    }

    public String getDetails() {
        return driver.findElement(body).getText().trim();
    }

    // Test case: verify Purchase priced correctly
    // actual amount = expected amount
    public int getAmountUsd() {
        String text = getDetails();

        Matcher m = Pattern.compile("Amount:\\s*(\\d+)\\s*USD").matcher(text);
        if (!m.find()) throw new IllegalStateException("Amount not found in confirmation: " + text);
        LogsUtils.info("Extracted Amount from confirmation: " + m.group(1));
        return Integer.parseInt(m.group(1));

    }

    public boolean verifyOrderAmount() {
        int expectedAmount = 0;
        for (int price : HomePage.getCollectedPricesForSelectedProducts()) {
            expectedAmount += price;
        }
        int actualTotal = getAmountUsd();
        if (expectedAmount == actualTotal) {
            LogsUtils.info("Expected total: " + expectedAmount + " | Actual: " + actualTotal);
            return true;
        } else {
            LogsUtils.error("Expected total: " + expectedAmount + " | Actual: " + actualTotal);
            return false;
        }

    }

    // Test case: verify Order ID for the purchase
    // expected: Order ID is extracted from the confirmation text
    public String extractOrderId(String text) {
        Matcher m = Pattern.compile("Id:\\s*(\\d+)").matcher(text);
        if (m.find()) {
            return m.group(1);
        }
        throw new IllegalArgumentException("Order ID not found in text: " + text);
    }

    public boolean verifyOrderId() {
        if (extractOrderId(getDetails()) == null || extractOrderId(getDetails()).isEmpty()) {
            throw new IllegalArgumentException("Text cannot be null or empty");
        } else
            return true;
    }


    // Test case: order is done and referring user to the home page
    //verifyRedirectToHomePageAfterPurchaseDone
    public HomePage clickOk() {
        driver.findElement(okBtn).click();
        WaitUtils.waitForElementToDisappear(driver, dialog);
        LogsUtils.info("Clicked OK button in Purchase Confirmation.");
        return new HomePage(driver);


    }

    public boolean verifyRedirectToHomePage() {
        try {
            WaitUtils.waitForElementToDisappear(driver, dialog);
            String currentUrl = driver.getCurrentUrl();
            if (currentUrl.equals(DataUtils.getPropertyData("environments", "LAUNCH_URL"))) {
                LogsUtils.info("Redirected to Home Page after purchase.");
                return true;
            } else {
                LogsUtils.error("Not redirected to Home Page. Current URL: " + currentUrl);
                return false;
            }
        } catch (Exception e) {
            LogsUtils.error("Error during redirect verification: " + e.getMessage());
            return false;
        }
    }


}
