package Tests;

import DriverFactory.BaseTest;
import Pages.HomePage;
import Pages.Modals.LoginModal;
import Pages.Modals.PlaceOrderModal;
import Pages.Modals.PurchaseConfirmationModal;
import Utilities.DataUtils;
import io.qameta.allure.*;
import listeners.IInvokedMethodListeners;
import listeners.ITestMethodListeners;
import org.testng.Assert;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.util.HashMap;
import java.util.Map;

@Epic("Order Placement Module")
@Feature("Place Order Modal & Purchase Flow")
@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class PlaceOrderTests extends BaseTest {
    private final String PASSWORD = DataUtils.getJsonData("authValidData", "password");
    private final String USERNAME = DataUtils.getJsonData("authValidData", "username");
    private final String PhonesCategory = DataUtils.getJsonDataFromList("categoryData", "categories[0]");
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
    public void verifyPlaceOrderWithValidData() {
        int productsToAdd = 2;
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
        Assert.assertTrue(new PurchaseConfirmationModal(getDriver()).verifyOrderId());
    }

    @Story("Prevent purchase when name is missing")
    @Description("Verify that leaving the Name field empty displays an appropriate alert.")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 2)
    public void verifyPlaceOrderWithEmptyName() {
        Map<String, String> data = new HashMap<>(DataUtils.getJsonDataAsMap("TestData/info.json"));
        data.put("name", "");

        new HomePage(getDriver())
                .addProductsToCart(2)
                .goToCart()
                .clickPlaceOrderButton()
                .fillingPlaceOrderModal(
                        data.get("name"),
                        data.get("country"),
                        data.get("city"),
                        data.get("creditCard"),
                        data.get("month"),
                        data.get("year")
                )
                .clickPurchaseButton();
        Assert.assertTrue(new PlaceOrderModal(getDriver()).assertAlertForEmptyNameField(),
                "Alert for empty name field is not displayed as expected.");
    }


    @Story("Prevent purchase when Card year is missing")
    @Description("Verify that leaving the Year field empty displays an alert. Fails if purchase still goes through.")
    @Severity(SeverityLevel.NORMAL)
    @Test(priority = 3)
    public void verifyAlertForEmptyYearField() {
        Map<String, String> data = new HashMap<>(DataUtils.getJsonDataAsMap("TestData/info.json"));
        data.put("year", "");

        new HomePage(getDriver())
                .addProductsToCart(1)
                .goToCart()
                .clickPlaceOrderButton()
                .fillingPlaceOrderModal(
                        data.get("name"),
                        data.get("country"),
                        data.get("city"),
                        data.get("creditCard"),
                        data.get("month"),
                        data.get("year")
                )
                .clickPurchaseButton();
        Assert.assertTrue(new PlaceOrderModal(getDriver()).assertAlertForEmptyYearField(),
                "Alert for empty year field is not displayed ,Purchase confirmed.");


    }

    @Story("Prevent purchase with invalid credit card number")
    @Description("Verify that entering an invalid credit card shows an alert. Fails if purchase still goes through.")
    @Severity(SeverityLevel.CRITICAL)
    @Test(priority = 4)
    public void verifyAlertForInvalidCreditCardNumber() {
        Map<String, String> data = new HashMap<>(DataUtils.getJsonDataAsMap("TestData/info.json"));
        data.put("creditCard", "@@@@@@@@@");

        new HomePage(getDriver())
                .addProductsToCart(2)
                .goToCart()
                .clickPlaceOrderButton()
                .fillingPlaceOrderModal(
                        data.get("name"),
                        data.get("country"),
                        data.get("city"),
                        data.get("creditCard"),
                        data.get("month"),
                        data.get("year")
                )
                .clickPurchaseButton();
        Assert.assertTrue(new PlaceOrderModal(getDriver()).assertAlertForInvalidCreditCardNumber(),
                "Alert for invalid credit card number is not displayed ,Purchase confirmed with wrong Credit card number.");


    }


}
