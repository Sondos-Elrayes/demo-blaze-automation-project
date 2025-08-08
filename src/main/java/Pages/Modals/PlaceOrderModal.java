package Pages.Modals;

import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class PlaceOrderModal {
    private final By placeOrderModal = By.cssSelector("[id=\"orderModalLabel\"]");
    private final By nameInput = By.id("name");
    private final By countryInput = By.id("country");
    private final By cityInput = By.id("city");
    private final By creditCardInput = By.id("card");
    private final By monthInput = By.id("month");
    private final By yearInput = By.id("year");
    private final By purchaseButton = By.xpath("//button[text()='Purchase']");
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


}
