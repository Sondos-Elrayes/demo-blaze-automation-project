package Pages.Modals;

import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import java.util.Objects;

public class PlaceOrderModal {
    private final By placeOrderModal = By.cssSelector("[id=\"orderModalLabel\"]");
    private final By nameInput = By.id("name");
    private final By countryInput = By.id("country");
    private final By cityInput = By.id("city");
    private final By creditCardInput = By.id("card");
    private final By monthInput = By.id("month");
    private final By yearInput = By.id("year");
    private final By purchaseButton = By.cssSelector("[onclick=\"purchaseOrder()\"]");
    private final By confirmationDialog = By.cssSelector(".sweet-alert.showSweetAlert.visible");
    private final By closeButton = By.cssSelector("div[id='orderModal'] div[class='modal-footer'] button:nth-child(1)");
    WebDriver driver;

    public PlaceOrderModal(WebDriver driver) {
        this.driver = driver;
    }

    public PlaceOrderModal fillingPlaceOrderModal(String name, String country, String city, String creditCard, String month, String year) {
        WaitUtils.waitForElementToBeVisible(driver, placeOrderModal);

        try {
            InteractionsUtils.enterText(driver, nameInput, name);
            InteractionsUtils.enterText(driver, countryInput, country);
            InteractionsUtils.enterText(driver, cityInput, city);
            InteractionsUtils.enterText(driver, creditCardInput, creditCard);
            InteractionsUtils.enterText(driver, monthInput, month);
            InteractionsUtils.enterText(driver, yearInput, year);
            LogsUtils.info("Filled Place Order Modal "
                    + "Name: " + name
                    + ", Country: " + country
                    + ", City: " + city
                    + ", Credit Card: " + creditCard
                    + ", Month: " + month
                    + ", Year: " + year);
        } catch (Exception e) {
            LogsUtils.error("Error filling Place Order Modal: " + e.getMessage());
        }
        return this;
    }

    public PurchaseConfirmationModal clickPurchaseButton() {
        WaitUtils.waitForElementToBeVisible(driver, purchaseButton);
        InteractionsUtils.clickOnElement(driver, purchaseButton);
        LogsUtils.info("Clicked Purchase button in Place Order Modal.");
        return new PurchaseConfirmationModal(driver);
    }

    public boolean assertAlertForEmptyNameField() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, purchaseButton);
            InteractionsUtils.clickOnElement(driver, purchaseButton);
            WaitUtils.waitAlert(driver);
            String alertText = driver.switchTo().alert().getText();
            driver.switchTo().alert().accept();
            if (alertText.equals("Please fill out Name and Creditcard.")) {
                LogsUtils.info("Alert for empty fields is displayed: " + alertText);
                return true;
            } else {
                LogsUtils.error("Unexpected alert message: " + alertText);
                return false;
            }
        } catch (Exception e) {
            LogsUtils.error("Error checking alert for empty fields: " + e.getMessage());
            return false;
        }
    }


    public boolean assertAlertForEmptyYearField() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, purchaseButton);
            InteractionsUtils.clickOnElement(driver, purchaseButton);

            //Check if confirmation modal is visible
            if (Objects.requireNonNull(WaitUtils.waitForElementToBeVisible(driver, confirmationDialog)).isDisplayed()) {
                LogsUtils.error("Purchase confirmation modal appeared — should have shown alert for invalid credit card.");
                return false;
            }

            //Check for alert
            if (InteractionsUtils.isAlertPresent(driver)) {
                String alertText = driver.switchTo().alert().getText();
                driver.switchTo().alert().accept();
                if ("Please fill out Year field.".equals(alertText)) {
                    LogsUtils.info("Correct alert displayed: " + alertText);
                    return true;
                } else {
                    LogsUtils.error("Unexpected alert message: " + alertText);
                    return false;
                }
            }
        } catch (Exception e) {
            LogsUtils.error("Error checking alert for invalid credit card: " + e.getMessage());
        }
        return false;

    }

    public boolean assertAlertForInvalidCreditCardNumber() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, purchaseButton);
            InteractionsUtils.clickOnElement(driver, purchaseButton);

            //Check if confirmation modal is visible
            if (Objects.requireNonNull(WaitUtils.waitForElementToBeVisible(driver, confirmationDialog)).isDisplayed()) {
                LogsUtils.error("Purchase confirmation modal appeared — should have shown alert for invalid credit card.");
                return false;
            }

            //Check for alert
            if (InteractionsUtils.isAlertPresent(driver)) {
                String alertText = driver.switchTo().alert().getText();
                driver.switchTo().alert().accept();
                if ("Please fill out Credit Card field with valid data.".equals(alertText)) {
                    LogsUtils.info("Correct alert displayed: " + alertText);
                    return true;
                } else {
                    LogsUtils.error("Unexpected alert message: " + alertText);
                    return false;
                }
            }
        } catch (Exception e) {
            LogsUtils.error("Error checking alert for invalid credit card: " + e.getMessage());
        }
        return false;

    }
}
