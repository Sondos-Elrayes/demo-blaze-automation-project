package Tests;

import DriverFactory.BaseTest;
import Pages.CartPage;
import Pages.HomePage;
import Pages.Modals.LoginModal;
import Pages.Modals.PurchaseConfirmationModal;
import Utilities.DataUtils;
import io.qameta.allure.*;
import listeners.IInvokedMethodListeners;
import listeners.ITestMethodListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

@Epic("Checkout Module")
@Feature("Order Completion")
@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class FinishOrderTests extends BaseTest {
    private final String PASSWORD = DataUtils.getJsonData("authValidData", "password");
    private final String USERNAME = DataUtils.getJsonData("authValidData", "username");
    private final String LaptopsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[1]");
    private final String name = DataUtils.getJsonData("info", "name");
    private final String country = DataUtils.getJsonData("info", "country");
    private final String city = DataUtils.getJsonData("info", "city");
    private final String card = DataUtils.getJsonData("info", "creditCard");
    private final String month = DataUtils.getJsonData("info", "month");
    private final String year = DataUtils.getJsonData("info", "year");

    @Severity(SeverityLevel.CRITICAL)
    @Story("User completes purchase successfully and sees correct confirmation")
    @Description("Verify that after placing an order, the confirmation modal shows correct thank you message and order amount.")
    @Test(priority = 1)
    public void verifyOrderFinishedSuccessfully() {
        int productsToAdd = 3;
        new LoginModal(getDriver())
                .clickLoginNavigationButton()
                .enterUsernameForLogin(USERNAME)
                .enterPasswordForLogin(PASSWORD)
                .clickLoginButton()
                .waitForWelcomeMessage()
                .chooseCategory(LaptopsCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart()
                .clickPlaceOrderButton()
                .fillingPlaceOrderModal(name, country, city, card, month, year)
                .clickPurchaseButton()
                .waitUntilPurchaseConfirmationOpen();

        SoftAssert softAssert = new SoftAssert();
        softAssert.assertTrue(new PurchaseConfirmationModal(getDriver()).assertThankYouMessage(),
                "Order was not placed successfully, thank you message is not displayed as expected.");
        softAssert.assertTrue(new PurchaseConfirmationModal(getDriver()).verifyOrderAmount(),
                "Order amount is not as expected.");
        softAssert.assertAll();
    }


    @Severity(SeverityLevel.MINOR)
    @Story("User is redirected to home page after completing purchase")
    @Description("Verify that user is redirected to home page after completing an order.")
    @Test(priority = 2)
    public void verifyWhenOrderFinishedRedirectToHomePage() {
        int productsToAdd = 3;
        new HomePage(getDriver())
                .addProductsToCart(productsToAdd)
                .goToCart()
                .clickPlaceOrderButton()
                .fillingPlaceOrderModal(name, country, city, card, month, year)
                .clickPurchaseButton()
                .waitUntilPurchaseConfirmationOpen()
                .clickOk();

        Assert.assertTrue(new PurchaseConfirmationModal(getDriver()).verifyRedirectToHomePage(),
                "User was not redirected to the home page after order completion.");
    }

    @Severity(SeverityLevel.NORMAL)
    @Story("Cart is emptied after order is completed")
    @Description("Verify that the cart is empty after order completion.")
    @Test(priority = 3)

    public void verifyWhenOrderFinishedCartIsEmpty() {
        int productsToAdd = 3;
        new HomePage(getDriver())
                .addProductsToCart(productsToAdd)
                .goToCart()
                .clickPlaceOrderButton()
                .fillingPlaceOrderModal(name, country, city, card, month, year)
                .clickPurchaseButton()
                .waitUntilPurchaseConfirmationOpen()
                .clickOk()
                .goToCart();

        Assert.assertTrue(new CartPage(getDriver()).cartItemDisappearedAfterPurchasing(),
                "Cart is not empty after order completion, items still present in the cart.");
    }


}
