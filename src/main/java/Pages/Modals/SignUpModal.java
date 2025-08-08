package Pages.Modals;

import Utilities.InteractionsUtils;
import Utilities.LogsUtils;
import Utilities.WaitUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebDriver;

public class SignUpModal {
    private final By signUpLink = By.id("signin2");
    private final By signUpUsername = By.id("sign-username");
    private final By signUpPassword = By.id("sign-password");
    private final By signUpButton = By.xpath("//button[text()='Sign up']");
    private final By logoutLink = By.id("logout2");
    private final WebDriver driver;

    public SignUpModal(WebDriver driver) {
        this.driver = driver;
    }

    //sign up methods
    public SignUpModal clickSignUpNavigationButton() {
        if (InteractionsUtils.isElementDisplayed(driver, signUpLink)) {
            InteractionsUtils.clickOnElement(driver, signUpLink);

        } else if (InteractionsUtils.isElementDisplayed(driver, logoutLink)) {
            InteractionsUtils.clickOnElement(driver, logoutLink);
            WaitUtils.waitForElementToBeVisible(driver, signUpLink);
            InteractionsUtils.clickOnElement(driver, signUpLink);

        } else {
            LogsUtils.error("Neither Sign Up nor Logout link is visible.");
            throw new NoSuchElementException("Sign Up and Logout links are missing.");
        }
        return this;
    }

    public SignUpModal enterUsernameForSignUp(String username) {
        InteractionsUtils.enterText(driver, signUpUsername, username);
        return this;
    }

    public SignUpModal enterPasswordForSignUp(String password) {
        InteractionsUtils.enterText(driver, signUpPassword, password);
        return this;
    }

    public SignUpModal clickSignUpButton() {
        InteractionsUtils.clickOnElement(driver, signUpButton);
        return this;
    }

    //test case if the message in alert tells that the user is successfully signed up
    //success message is "Sign up successful."
    //unsuccess message is "This user already exist."
    //bug empty password but username is filled "Please fill out Username and Password."
    //expected alert message is "please enter at least 5 characters for username and password."
    public String getSignUpMessageAlert() {
        WaitUtils.waitAlert(driver);
        String alertText = driver.switchTo().alert().getText();
        driver.switchTo().alert().accept();
        return alertText;
    }
    //bug - if the user tries to sign up with an existing password, the alert will show that the user already exists

}
