package Pages;

import Utilities.DataUtils;
import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;

import java.util.ArrayList;
import java.util.List;

import static DriverFactory.DriverFactory.getDriver;
import static Utilities.InteractionsUtils.findWebElement;
import static Utilities.WaitUtils.generalWait;
import static Utilities.WaitUtils.waitForElementToBeVisible;

public class HomePage {
    public static final List<String> expectedTitlesForSelectedProducts = new ArrayList<>();
    public static final List<Integer> collectedPricesForSelectedProducts = new ArrayList<>();
    public final String BASE_URL = DataUtils.getPropertyData("environments", "LAUNCH_URL");
    private final WebDriver driver;
    private final By categories = By.cssSelector("a[id=\"cat\"]");
    private final By phonesCategory = By.cssSelector("[onclick=\"byCat('phone')\"]");
    private final By laptopsCategory = By.cssSelector("[onclick=\"byCat('notebook')\"]");
    private final By monitorsCategory = By.cssSelector("[onclick=\"byCat('monitor')\"]");
    private final By productImages = By.cssSelector(".card-img-top.img-fluid");
    private final By productLink = By.cssSelector("[class=\"hrefch\"]");
    private final By cartLink = By.xpath("//*[@id=\"cartur\"]");
    private final By productLinksLocator = By.cssSelector("#tbodyid .card-title a");
    private final By waitForProductLinks = By.xpath("//*[@id=\"tbodyid\"]/div/div/div/h4/a");

    public HomePage(WebDriver driver) {
        this.driver = driver;
    }

    public static List<Integer> getCollectedPricesForSelectedProducts() {
        return collectedPricesForSelectedProducts;
    }

    public List<WebElement> allProductImages() {
        waitForElementToBeVisible(getDriver(), productImages);
        return driver.findElements(productImages);
    }

    public List<WebElement> allProductLinks() {
        waitForElementToBeVisible(getDriver(), productLink);
        return driver.findElements(productLink);
    }


    public HomePage chooseCategory(String category) {
        waitForElementToBeVisible(getDriver(), productLinksLocator);
        switch (category.toLowerCase()) {
            case "phones":
                findWebElement(driver, phonesCategory).click();
                break;
            case "laptops":
                findWebElement(driver, laptopsCategory).click();
                break;
            case "monitors":
                findWebElement(driver, monitorsCategory).click();
                break;
            default:
                LogsUtils.error("Invalid category clicked");

        }
        WaitUtils.waitForPresenceOfAllElements(driver, waitForProductLinks);
        return this;
    }

    public HomePage verifyAllProductLinksAreWorking() {
        LogsUtils.info("Checking all product links on the homepage.");
        WaitUtils.waitForPresenceOfAllElements(driver, waitForProductLinks);
        List<String> hrefs = allProductLinks()
                .stream()
                .map(e -> e.getAttribute("href"))
                .toList();

        List<String> invalidLinks = InteractionsUtils
                .checkIfLinksAreWorking(hrefs, BASE_URL);

        if (!invalidLinks.isEmpty()) {
            LogsUtils.error("Some product links are not working:");
            for (String invalid : invalidLinks) {
                LogsUtils.error("Invalid: " + invalid);
            }
        } else {
            LogsUtils.info("All product links are working correctly.");
        }
        return this;
    }

    public boolean checkAllProductLinks() {
        WaitUtils.waitForPresenceOfAllElements(driver, waitForProductLinks);
        List<String> hrefs = allProductLinks()
                .stream()
                .map(e -> e.getAttribute("href"))
                .toList();

        return InteractionsUtils.checkIfLinksAreWorking(hrefs, BASE_URL).isEmpty();
    }

    // TODO: Implement test case to verify product images
    public HomePage verifyAllProductImageAreWorking() {
        LogsUtils.info("Checking all product images on the homepage.");
        waitForElementToBeVisible(getDriver(), productImages);
        List<String> invalidImageLink = InteractionsUtils
                .checkIfLinksAreWorking(allProductImages(), BASE_URL, "photo");

        if (!invalidImageLink.isEmpty()) {
            LogsUtils.error("Some product images are not working:");
            for (String invalid : invalidImageLink) {
                LogsUtils.error("Invalid: " + invalid);
            }
        } else {
            LogsUtils.info("All product images are working correctly.");
        }
        return this;
    }

    // TODO: Implement test case to choose specific product
    public ProductPage ChooseProduct(String product) {
        waitForElementToBeVisible(getDriver(), productLink);
        String xpath = "//a[text()='" + product + "']";
        InteractionsUtils.clickOnElement(driver, productLink);
        LogsUtils.info("Product selected: " + product);
        return new ProductPage(driver);

    }

    public HomePage addProductsToCart(int numberOfProducts) {

        expectedTitlesForSelectedProducts.clear();
        collectedPricesForSelectedProducts.clear();

        LogsUtils.info("Adding " + numberOfProducts + " products to cart.");
        for (int i = 0; i < numberOfProducts; i++) {
            waitForElementToBeVisible(driver, productLinksLocator);

            // Refetch the elements each time (page reloads after navigate().back())
            List<WebElement> products = driver.findElements(productLinksLocator);

            if (i >= products.size()) {
                LogsUtils.error("Requested product index " + i + " exceeds available products (" + products.size() + ")");
                break;
            }

            WebElement product = products.get(i);
            expectedTitlesForSelectedProducts.add(product.getText());
            product.click();
            LogsUtils.info("Product " + i + " " + expectedTitlesForSelectedProducts.get(i) + " opened for adding to cart.");


            ProductPage productPage = new ProductPage(driver);
            int price = productPage.getProductPrice();
            collectedPricesForSelectedProducts.add(price);
            productPage.addToCart();
            productPage.waitForAddToCartAlertAndAccept();

            driver.navigate().back(); // back
            driver.navigate().back();// back to homepage
            WaitUtils.waitForPresenceOfAllElements(driver, productLinksLocator);
        }

        LogsUtils.info("All " + numberOfProducts + " products added to cart successfully.");
        return this;
    }

    private int getCartItemCount() {
        String countText = driver.findElement(By.id("cartur")).getText().replaceAll("\\D+", "");
        return countText.isEmpty() ? 0 : Integer.parseInt(countText);
    }


    public boolean VerifyCartPageURL(String expectedURL) {
        try {
            generalWait(driver).until(ExpectedConditions.urlToBe(expectedURL));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public CartPage goToCart() {
        InteractionsUtils.clickOnElement(driver, cartLink);
        LogsUtils.info("Navigated to Cart page.");
        return new CartPage(driver);
    }
}
