package Tests;

import Pages.CartPage;
import Pages.HomePage;
import Pages.Modals.LoginModal;
import Utilities.DataUtils;
import Utilities.LogsUtils;
import io.qameta.allure.Description;
import listeners.IInvokedMethodListeners;
import listeners.ITestMethodListeners;
import org.testng.Assert;
import org.testng.annotations.AfterTest;
import org.testng.annotations.BeforeTest;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

import static DriverFactory.DriverFactory.*;

@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class HomeTests {
    private final String PASSWORD = DataUtils.getJsonData("loginData", "password");
    private final String USERNAME = DataUtils.getJsonData("loginData", "username");
    private final String PhonesCategory = DataUtils.getJsonDataFromList("categoryData", "categories[0]");
    private final String LaptopsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[1]");
    private final String MonitorsCategory = DataUtils.getJsonDataFromList("categoryData", "categories[2]");


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
    public void cartProductCountTestPhonesCategory() {
        int productsToAdd = 3;
        new LoginModal(getDriver())
                .clickLoginNavigationButton()
                .enterUsernameForLogin(USERNAME)
                .enterPasswordForLogin(PASSWORD)
                .clickLoginButton()
                .waitForWelcomeMessage()
                .chooseCategory(PhonesCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart();

        Assert.assertTrue(new CartPage(getDriver())
                        .assertNumberOfProductsInCart(productsToAdd),
                "Cart product count does not match expected count: " + productsToAdd);
    }

    @Description("Verify that all products in the Laptops category are displayed and can be added to the cart correctly.")
    @Test
    public void cartProductCountTestLaptopsCategory() {
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

    @Description("Verify that all products in the Monitors category are displayed and can be added to the cart correctly.")
    @Test
    public void cartProductCountTestMonitorsCategory() {
        int productsToAdd = 2;
        new HomePage(getDriver())
                .chooseCategory(MonitorsCategory)
                .verifyAllProductLinksAreWorking()
                .addProductsToCart(productsToAdd)
                .goToCart();

        Assert.assertTrue(new CartPage(getDriver())
                        .assertNumberOfProductsInCart(productsToAdd),
                "Cart product count does not match expected count: " + productsToAdd);
    }

    @Test
    public void cliCkOnCartButton() {
        new HomePage(getDriver())
                .goToCart();
        Assert.assertTrue(new HomePage(getDriver())
                        .VerifyCartPageURL(DataUtils.getPropertyData("environments", "CART_URL")),
                "Cart page URL does not match expected URL.");


    }

    @AfterTest
    public void tearDown() {
        quitDriver();
    }
}
