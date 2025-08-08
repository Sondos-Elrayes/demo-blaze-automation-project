package Tests;

import Pages.Modals.LoginModal;
import Pages.Modals.SignUpModal;
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

import static DriverFactory.DriverFactory.*;

@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class AuthTests {

    private final String VALID_PASSWORD = DataUtils.getJsonData("authValidData", "password");
    private final String VALID_USERNAME = DataUtils.getJsonData("authValidData", "username");
    private final String INVALID_PASSWORD = DataUtils.getJsonData("authInvalidData", "password");
    private final String INVALID_USERNAME = DataUtils.getJsonData("authInvalidData", "username");

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
    public void validSignUpTest() {
        new SignUpModal(getDriver())
                .clickSignUpNavigationButton()
                .enterUsernameForSignUp(VALID_USERNAME)
                .enterPasswordForSignUp(VALID_PASSWORD)
                .clickSignUpButton();
        Assert.assertEquals(new SignUpModal(getDriver()).getSignUpMessageAlert(),
                "Sign up successful.");


    }

    @Test
    public void validLoginTest() {
        new LoginModal(getDriver())
                .clickLoginNavigationButton()
                .enterUsernameForLogin(VALID_USERNAME)
                .enterPasswordForLogin(VALID_PASSWORD)
                .clickLoginButton()
                .waitForWelcomeMessage();
        Assert.assertTrue(new LoginModal(getDriver()).assertLoginTC("Welcome " + VALID_USERNAME),
                "Login failed, welcome message is not displayed as expected.");

    }

    @AfterTest
    public void tearDown() {
        quitDriver();
    }
}


