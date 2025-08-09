package Tests;

import DriverFactory.BaseTest;
import Pages.CartPage;
import Pages.HomePage;
import Utilities.DataUtils;
import io.qameta.allure.*;
import listeners.IInvokedMethodListeners;
import listeners.ITestMethodListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Epic("Cart Module")
@Feature("Cart Management & Checkout")
@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class CartTests extends BaseTest {
    private final String LaptopsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[1]");
    private final String MonitorsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[2]");


    @Severity(SeverityLevel.NORMAL)
    @Story("Add products to cart and verify product count")
    @Description("Verify that the number of products in the cart matches the number of products added from a category.")
    @Test(priority = 1)
    public void cartProductCountTest() {
        int productsToAdd = 3;
        new HomePage(getDriver())
                .chooseCategory(LaptopsCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart();

        Assert.assertTrue(new CartPage(getDriver())
                        .assertNumberOfProductsInCart(productsToAdd),
                "Cart product count does not match expected count: " + productsToAdd);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("Validate cart product details")
    @Description("Validate that both product titles and prices in the cart match the selected products from the Home page.")
    @Test(priority = 2)
    public void verifyCartContentsTest() {
        int productsToAdd = 3;

        new HomePage(getDriver())
                .chooseCategory(LaptopsCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart();

        Assert.assertTrue(
                new CartPage(getDriver()).assertCartContents(),
                "Cart contents (titles or prices) do not match expected."
        );
    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("Verify cart total calculation")
    @Description("Ensure that the total price in the cart equals the sum of individual product prices added to the cart.")
    @Test(priority = 3)
    public void verifyCartTotalPriceMatchesSumProductPricesAddedToCart() {
        int productsToAdd = 3;
        new HomePage(getDriver())
                .chooseCategory(LaptopsCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart();
        Assert.assertTrue(
                new CartPage(getDriver()).isCartTotalCorrect(),
                "Cart total does not match the sum of added product prices."
        );
    }


    @Severity(SeverityLevel.NORMAL)
    @Story("Open checkout modal from cart")
    @Description("Verify that clicking 'Place Order' opens the Place Order modal when there are items in the cart.")
    @Test(priority = 4)
    public void verifyPlaceOrderModalDisplayedWithItemsInCart() {
        int productsToAdd = 2;

        new HomePage(getDriver())
                .chooseCategory(MonitorsCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart()
                .clickPlaceOrderButton();

        Assert.assertTrue(
                new CartPage(getDriver()).assertPlaceOrderModalDisplayedWithItemsInCart(),
                "Place Order modal is not displayed with items in the cart."
        );
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("Prevent checkout with empty cart")
    @Description("Verify that clicking 'Place Order' shows an error when the cart is empty.")
    @Test(priority = 5)
    public void verifyPlaceOrderModalNotDisplayedForEmptyCart() {
        int productsToAdd = 2;

        new HomePage(getDriver())
                .chooseCategory(MonitorsCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart()
                .clearCartItems()
                .clickPlaceOrderButton();

        Assert.assertTrue(
                new CartPage(getDriver()).assertPlaceOrderModalNotDisplayedForEmptyCart(),
                "Place Order modal should not be displayed for an empty cart."
        );
    }

}
