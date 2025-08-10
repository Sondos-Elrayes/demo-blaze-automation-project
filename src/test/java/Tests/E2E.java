package Tests;

import DriverFactory.BaseTest;
import Pages.Modals.LoginModal;
import Pages.Modals.PurchaseConfirmationModal;
import Utilities.DataUtils;
import io.qameta.allure.Description;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.Assert;
import org.testng.annotations.Test;

public class E2E extends BaseTest {
    private final String PASSWORD = DataUtils.getJsonData("authValidData", "password");
    private final String USERNAME = DataUtils.getJsonData("authValidData", "username");
    private final String LaptopsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[1]");
    private final String name = DataUtils.getJsonData("info", "name");
    private final String country = DataUtils.getJsonData("info", "country");
    private final String city = DataUtils.getJsonData("info", "city");
    private final String card = DataUtils.getJsonData("info", "creditCard");
    private final String month = DataUtils.getJsonData("info", "month");
    private final String year = DataUtils.getJsonData("info", "year");

    @Severity(SeverityLevel.BLOCKER)
    @Story("Complete purchase with valid data")
    @Description("Verify that a user can successfully place an order with valid details.")
    @Test(priority = 1)
    public void verifyEndToEndScenario() {
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
                .waitUntilPurchaseConfirmationOpen()
                .clickOk();
        Assert.assertTrue(new PurchaseConfirmationModal(getDriver()).verifyRedirectToHomePage(),
                "User was not redirected to the home page after order completion.");
    }
}
