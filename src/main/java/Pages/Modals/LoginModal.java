package Pages.Modals;

import Pages.HomePage;
import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class LoginModal {
    private final WebDriver driver;

    private final By loginLink = By.id("login2");
    private final By loginUsername = By.id("loginusername");
    private final By loginPassword = By.id("loginpassword");
    private final By loginButton = By.cssSelector("[onclick=\"logIn()\"]");
    private final By usernameLabel = By.id("nameofuser");
    private final By getLoginModal = By.id("logInModalLabel");
    private final By closeLoginModalButton = By.xpath("//div[@id='logInModal']//button[text()='Close']");

    private final By logoutLink = By.id("logout2");
    private final By cartLink = By.id("cartur");
    private final By homeLink = By.xpath("//a[@class='navbar-brand']");

    public LoginModal(WebDriver driver) {
        this.driver = driver;
    }


    // Login methods
    public LoginModal clickLoginNavigationButton() {
        if (InteractionsUtils.isElementDisplayed(driver, loginLink)) {
            // Login link is available, click it
            InteractionsUtils.clickOnElement(driver, loginLink);

        } else if (InteractionsUtils.isElementDisplayed(driver, logoutLink)) {
            // User is logged in, log out first
            InteractionsUtils.clickOnElement(driver, logoutLink);
            WaitUtils.waitForElementToBeVisible(driver, loginLink);
            InteractionsUtils.clickOnElement(driver, loginLink);

        } else {
            LogsUtils.error("Neither Login nor Logout link is visible.");
            throw new NoSuchElementException("Login and Logout links are missing.");
        }
        return this;
    }

    public LoginModal enterUsernameForLogin(String username) {
        InteractionsUtils.enterText(driver, loginUsername, username);
        return this;
    }

    public LoginModal enterPasswordForLogin(String password) {
        InteractionsUtils.enterText(driver, loginPassword, password);
        return this;
    }

    public LoginModal clickLoginButton() {
        InteractionsUtils.clickOnElement(driver, loginButton);
        return this;
    }

    public HomePage waitForWelcomeMessage() {
        WaitUtils.waitForElementToBeVisible(driver, usernameLabel);
        return new HomePage(driver);
    }

    public String welcomeMessage() {
        return InteractionsUtils.findWebElement(driver, usernameLabel).getText();
    }

    public boolean assertLoginTC(String expectedUsername) {
        String actualUsername = InteractionsUtils.findWebElement(driver, usernameLabel).getText();
        return actualUsername.equals(expectedUsername);

    }

    public String getLoginMessageAlert() {
        WaitUtils.waitAlert(driver);
        String alertText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        return alertText;
    }


}
