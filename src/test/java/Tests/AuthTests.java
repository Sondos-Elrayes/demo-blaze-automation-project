package Tests;

import Pages.Modals.LoginModal;
import Pages.Modals.SignUpModal;
import Utilities.DataUtils;
import Utilities.LogsUtils;
import io.qameta.allure.*;
import listeners.IInvokedMethodListeners;
import listeners.ITestMethodListeners;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

import java.time.Duration;

import static DriverFactory.DriverFactory.*;

@Epic("Authentication Module")
@Feature("Sign Up & Login")
@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class AuthTests {

    private final String VALID_PASSWORD = DataUtils.getJsonData("authValidData", "password");
    private final String VALID_USERNAME = DataUtils.getJsonData("authValidData", "username");
    private final String INVALID_PASSWORD = DataUtils.getJsonData("authInvalidData", "password");
    private final String INVALID_USERNAME = DataUtils.getJsonData("authInvalidData", "username");

    @BeforeMethod
    public void setup() {
        setupDriver(DataUtils.getPropertyData("environments", "BROWSER"));
        LogsUtils.info("chrome driver is set up successfully.");
        getDriver().get(DataUtils.getPropertyData("environments", "LAUNCH_URL"));
        LogsUtils.info("Application URL is launched successfully.");
        getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(5));

    }

    @Story("Sign up with valid data")
    @Description("Verify valid sign up with new username and password display alert message 'Sign up successful.'")
    @Test(priority = 1)
    public void validSignUpTest() {
        new SignUpModal(getDriver())
                .clickSignUpNavigationButton()
                .enterUsernameForSignUp(VALID_USERNAME)
                .enterPasswordForSignUp(VALID_PASSWORD)
                .clickSignUpButton();
        Assert.assertEquals(new SignUpModal(getDriver()).getSignUpMessageAlert(),
                "Sign up successful.",
                "Sign up failed, alert message is not as expected.");
        LogsUtils.info("Sign up successful, alert message is displayed as expected.");


    }

    @Severity(SeverityLevel.BLOCKER)
    @Story("Login with valid credentials")
    @Description("Verify valid login with correct username and password")
    @Test(priority = 2, alwaysRun = true)
    public void validLoginTest() {
        new LoginModal(getDriver())
                .clickLoginNavigationButton()
                .enterUsernameForLogin(VALID_USERNAME)
                .enterPasswordForLogin(VALID_PASSWORD)
                .clickLoginButton()
                .waitForWelcomeMessage();
        Assert.assertTrue(new LoginModal(getDriver()).assertLoginTC("Welcome " + VALID_USERNAME),
                "Login failed, welcome message is not displayed as expected.");
        LogsUtils.info("Login successful, welcome message is displayed as expected.");

    }


    @Severity(SeverityLevel.CRITICAL)
    @Story("Sign up with existing username")
    @Description("Verify invalid sign up with already exist username display alert message 'This user already exist.'")
    @Test(priority = 3)
    public void invalidSignUpTest() {
        new SignUpModal(getDriver())
                .clickSignUpNavigationButton()
                .enterUsernameForSignUp(INVALID_USERNAME)
                .enterPasswordForSignUp(VALID_PASSWORD)
                .clickSignUpButton();
        Assert.assertEquals(new SignUpModal(getDriver()).getSignUpMessageAlert(),
                "This user already exist.",
                "Alert message is not as expected.");
        LogsUtils.info("Sign up failed, alert message is displayed as expected with This user already exist message.");

    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("Login with wrong password")
    @Description("Verify invalid login with wrong password displays 'Wrong password.'")
    @Test(priority = 5)
    public void invalidLoginTest() {
        new LoginModal(getDriver())
                .clickLoginNavigationButton()
                .enterUsernameForLogin(VALID_USERNAME)
                .enterPasswordForLogin(VALID_PASSWORD + " ")
                .clickLoginButton();
        Assert.assertEquals(new LoginModal(getDriver()).getLoginMessageAlert(), "Wrong password.",
                "Alert message is not as expected.");
        LogsUtils.info("Login failed, alert message is displayed as expected with wrong password message.");
    }

    @Severity(SeverityLevel.MINOR)
    @Story("Sign up with empty password")
    @Description("Verify sign up with empty password display alert message 'Please fill out Password.'")
    @Test(priority = 4)
    public void signUpWithEmptyPasswordTest() {
        new SignUpModal(getDriver())
                .clickSignUpNavigationButton()
                .enterUsernameForSignUp(VALID_USERNAME)
                .enterPasswordForSignUp(INVALID_PASSWORD)
                .clickSignUpButton();
        Assert.assertEquals(new SignUpModal(getDriver()).getSignUpMessageAlert(),
                "Please fill out Password.",
                "Alert message is not as expected.");
        LogsUtils.info("Sign up failed, alert message is displayed as expected.");


    }

    @AfterMethod
    public void tearDown() {
        quitDriver();
    }
}


