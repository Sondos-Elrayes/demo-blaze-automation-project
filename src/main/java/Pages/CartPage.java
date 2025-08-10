package Pages;

import Pages.Modals.PlaceOrderModal;
import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CartPage {

    private final By cartRowsForElements = By.cssSelector("#tbodyid > tr");
    private final By productTitleCells = By.xpath("//tr/td[2]");
    private final By productPriceCells = By.xpath("//tr/td[3]");
    private final By firstRowDeleteLink = By.xpath("(//a[text()='Delete'])[1]");
    private final By totalPriceForCartProducts = By.id("totalp");
    private final By placeOrderButton = By.xpath("//button[@class='btn btn-success']");
    private final By placeOrderModal = By.cssSelector("[id=\"orderModalLabel\"]");
    WebDriver driver;

    public CartPage(WebDriver driver) {
        this.driver = driver;
    }

    public List<String> getProductTitlesInCart() {
        WaitUtils.waitForElementToBeVisible(driver, cartRowsForElements);
        List<WebElement> titles = driver.findElements(productTitleCells);
        List<String> titleTexts = new ArrayList<>();
        for (WebElement title : titles) {
            titleTexts.add(title.getText());
        }
        LogsUtils.info("Cart contains products: " + titleTexts);
        return titleTexts;
    }

    public List<Integer> getProductPricesInCart() {
        WaitUtils.waitForElementToBeVisible(driver, cartRowsForElements);
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

        List<String> expTitles = new ArrayList<>(HomePage.expectedTitlesForSelectedProducts);
        List<Integer> expPrices = new ArrayList<>(HomePage.collectedPricesForSelectedProducts);

        // Order-insensitive compare
        Collections.sort(expTitles);
        Collections.sort(actualTitles);
        Collections.sort(expPrices);
        Collections.sort(actualPrices);

        boolean titlesMatch = actualTitles.equals(expTitles);
        boolean pricesMatch = actualPrices.equals(expPrices);

        if (!titlesMatch) {
            LogsUtils.error("Titles mismatch.\nExpected: " + expTitles + "\nActual: " + actualTitles);
        }
        if (!pricesMatch) {
            LogsUtils.error("Prices mismatch.\nExpected: " + expPrices + "\nActual: " + actualPrices);
        }
        return titlesMatch && pricesMatch;
    }


    public boolean assertNumberOfProductsInCart(int expectedCount) {
        WaitUtils.waitForElementToBeVisible(driver, cartRowsForElements);
        try {
            List<WebElement> rows = driver.findElements(cartRowsForElements);
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

    public PlaceOrderModal clickPlaceOrderButton() {
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

    public CartPage clearCartItems() {
        // Refetch row list after each delete to avoid stale elements
        List<WebElement> rows = driver.findElements(cartRowsForElements);
        while (!rows.isEmpty()) {
            int before = rows.size();
            // Click "Delete" in the first row
            InteractionsUtils.clickOnElement(driver, firstRowDeleteLink);

            // Wait until one row disappears
            new WebDriverWait(driver, Duration.ofSeconds(10))
                    .until(d -> driver.findElements(cartRowsForElements).size() == before - 1);

            rows = driver.findElements(cartRowsForElements);
        }
        return this;
    }

    public boolean assertPlaceOrderModalDisplayedWithItemsInCart() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, placeOrderModal);
            LogsUtils.info("Place Order modal is displayed after clicking Place Order button.");
            return true;

        } catch (Exception e) {
            LogsUtils.error("Error while clicking Place Order button: " + e.getMessage());
            return false;
        }

    }

    public boolean assertPlaceOrderModalNotDisplayedForEmptyCart() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, placeOrderModal);
            LogsUtils.error("Place Order modal should not be displayed for empty cart.");
            return false;
        } catch (Exception e) {
            LogsUtils.info("Place Order modal is not displayed for empty cart as expected.");
            return true;
        }


    }

    public boolean cartItemDisappearedAfterPurchasing() {
        return !InteractionsUtils.isElementDisplayed(driver, cartRowsForElements);
    }

}
