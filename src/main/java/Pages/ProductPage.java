package Pages;

import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class ProductPage {
    private final By addToCartButton = By.xpath("//a[text()='Add to cart']");
    private final By ProductPrice = By.className("price-container");
    WebDriver driver;


    public ProductPage(WebDriver driver) {
        this.driver = driver;
    }

    public int getProductPrice() {
        try {
            WaitUtils.waitForElementToBeVisible(driver, ProductPrice);
            String priceText = driver.findElement(ProductPrice).getText();
            return Integer.parseInt(priceText.replaceAll("[^\\d]", "")); // returns 790
        } catch (Exception e) {
            LogsUtils.error("Error while getting product price: " + e.getMessage());
            return 0;
        }

    }


    public ProductPage addToCart() {
        InteractionsUtils.clickOnElement(driver, addToCartButton);
        LogsUtils.info("Clicked 'Add to cart' button.");
        return this;
    }

    public ProductPage waitForAddToCartAlertAndAccept() {
        WaitUtils.waitAlert(driver);
        String alertText = driver.switchTo().alert().getText();
        LogsUtils.info("Alert appeared: " + alertText);
        driver.switchTo().alert().accept();
        return this;
    }
}
