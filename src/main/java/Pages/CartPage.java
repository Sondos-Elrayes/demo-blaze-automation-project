package Pages;

import Pages.Modals.PlaceOrderModal;
import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.ArrayList;
import java.util.List;

public class CartPage {

    private final By cartElements = By.cssSelector("#tbodyid > tr");
    private final By productTitleCells = By.xpath("//tr/td[2]");
    private final By productPriceCells = By.xpath("//tr/td[3]");
    private final By totalPriceForCartProducts = By.id("totalp");
    private final By placeOrderButton = By.xpath("//button[@class='btn btn-success']");
    WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getProductTitlesInCart() {
        List<WebElement> titles = driver.findElements(productTitleCells);
        List<String> titleTexts = new ArrayList<>();
        for (WebElement title : titles) {
            titleTexts.add(title.getText());
        }
        LogsUtils.info("Cart contains products: " + titleTexts);
        return titleTexts;
    }

    public List<Integer> getProductPricesInCart() {
        List<WebElement> prices = driver.findElements(productPriceCells);
        List<Integer> priceValues = new ArrayList<>();
        for (WebElement price : prices) {
            priceValues.add(Integer.parseInt(price.getText()));
        }
        LogsUtils.info("Cart contains prices: " + priceValues);
        return priceValues;
    }

    public boolean assertCartContents() {
        List<String> actualTitles = getProductTitlesInCart();
        List<Integer> actualPrices = getProductPricesInCart();

        boolean titlesMatch = actualTitles.equals(HomePage.expectedTitlesForSelectedProducts);

        boolean pricesMatch = actualPrices.equals(HomePage.collectedPricesForSelectedProducts);

        if (!titlesMatch) {
            LogsUtils.error("Product titles in cart do not match expected.\nExpected: "
                    + HomePage.expectedTitlesForSelectedProducts + "\nActual: " + actualTitles);
        }

        if (!pricesMatch) {
            LogsUtils.error("Product prices in cart do not match expected.\nExpected: "
                    + HomePage.collectedPricesForSelectedProducts + "\nActual: " + actualPrices);
        }

        return titlesMatch && pricesMatch;
    }

    public boolean assertNumberOfProductsInCart(int expectedCount) {
        WaitUtils.waitForElementToBeVisible(driver, cartElements);
        try {
            List<WebElement> rows = driver.findElements(cartElements);
            int actualCount = rows.size();
            LogsUtils.info("Expected products: " + expectedCount + ", Actual in cart: " + actualCount);

            if (actualCount == expectedCount) {
                LogsUtils.info("Cart product count matches expected.");
                return true;
            } else {
                LogsUtils.error("Cart product count does not match. Expected: " + expectedCount + ", Actual: " + actualCount);
                return false;
            }

        } catch (Exception e) {
            LogsUtils.error("Exception during cart product count check: " + e.getMessage());
            return false;
        }
    }

    public int getDisplayedTotalPrice() {
        WaitUtils.waitForElementToBeVisible(driver, totalPriceForCartProducts);
        if (!driver.findElement(totalPriceForCartProducts).getText().isEmpty()) {
            return Integer.parseInt(driver.findElement(totalPriceForCartProducts).getText().trim());
        }
        LogsUtils.error("Total price element not found or empty.");
        return 0;

    }

    public boolean isCartTotalCorrect() {
        int expectedTotal = 0;

        for (int price : HomePage.getCollectedPricesForSelectedProducts()) {
            expectedTotal += price;
        }
        int actualTotal = getDisplayedTotalPrice();

        LogsUtils.info("Expected total: " + expectedTotal + " | Actual: " + actualTotal);
        return actualTotal == expectedTotal;
    }

    public PlaceOrderModal placeOrderButton() {
        WaitUtils.waitForElementToBeVisible(driver, placeOrderButton);
        try {
            InteractionsUtils.clickOnElement(driver, placeOrderButton);
        } catch (Exception e) {
            LogsUtils.error("Failed to click Place Order button: " + e.getMessage());
            return null;
        }
        LogsUtils.info("Clicked on Place Order button.");
        return new PlaceOrderModal(driver);
    }

    public boolean cartItemDisappearedAfterPurchasing() {
        return assertNumberOfProductsInCart(0);
    }

}
