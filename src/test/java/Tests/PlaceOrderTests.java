package Tests;

import Pages.HomePage;
import Pages.Modals.LoginModal;
import Pages.Modals.PurchaseConfirmationModal;
import Utilities.DataUtils;
import Utilities.LogsUtils;
import listeners.IInvokedMethodListeners;
import listeners.ITestMethodListeners;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

import static DriverFactory.DriverFactory.*;

@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class PlaceOrderTests {
    private final String PASSWORD = DataUtils.getJsonData("loginData", "password");
    private final String USERNAME = DataUtils.getJsonData("loginData", "username");
    private final String PhonesCategory = DataUtils.getJsonDataFromList("categoryData", "categories[0]");
    private final String LaptopsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[1]");
    private final String MonitorsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[2]");
    private final String name = DataUtils.getJsonData("info", "name");
    private final String country = DataUtils.getJsonData("info", "country");
    private final String city = DataUtils.getJsonData("info", "city");
    private final String card = DataUtils.getJsonData("info", "creditCard");
    private final String month = DataUtils.getJsonData("info", "month");
    private final String year = DataUtils.getJsonData("info", "year");


    @BeforeTest
    public void setup() {
        setupDriver(DataUtils.getPropertyData("environments", "BROWSER"));
        LogsUtils.info("chrome driver is set up successfully.");
        getDriver().get(DataUtils.getPropertyData("environments", "LAUNCH_URL"));
        LogsUtils.info("Application URL is launched successfully.");
        getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(5));
    }


    @Test
    public void verifyPlaceOrderWithValidData() {
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
        Assert.assertTrue(new PurchaseConfirmationModal(getDriver()).verifyOrderId());
    }

    @Test
    public void verifyPlaceOrderWithEmptyName() {
        Map<String, String> data = new HashMap<>(DataUtils.getJsonDataAsMap("info.json"));
        data.put("name", "");

        new HomePage(getDriver())
                .chooseCategory(LaptopsCategory)
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


    }

    @AfterTest
    public void tearDown() {
        quitDriver();
    }
}
