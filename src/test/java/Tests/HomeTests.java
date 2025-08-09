package Tests;

import DriverFactory.BaseTest;
import Pages.HomePage;
import Utilities.DataUtils;
import io.qameta.allure.*;
import listeners.IInvokedMethodListeners;
import listeners.ITestMethodListeners;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Listeners;
import org.testng.annotations.Test;

@Epic("Products & Categories Module")
@Feature("Category Browsing & Cart Access")
@Listeners({IInvokedMethodListeners.class, ITestMethodListeners.class})
public class HomeTests extends BaseTest {
    private final String PASSWORD = DataUtils.getJsonData("authValidData", "password");
    private final String USERNAME = DataUtils.getJsonData("authValidData", "username");


    @DataProvider(name = "categoriesData")
    public Object[][] categories() {
        return new Object[][]{
                {DataUtils.getJsonDataFromList("categoryData", "categories[0]")},
                {DataUtils.getJsonDataFromList("categoryData", "categories[1]")},
                {DataUtils.getJsonDataFromList("categoryData", "categories[2]")}
        };
    }

    @Severity(SeverityLevel.MINOR)
    @Story("Browse by category and verify product links")
    @Description("Verify that for each category, all product links on the Home page are working and accessible")
    @Test(priority = 1, dataProvider = "categoriesData")
    public void cartProductCountTest(String category) {
        new HomePage(getDriver())
                .chooseCategory(category)
                .verifyAllProductLinksAreWorking();
        Assert.assertTrue(new HomePage(getDriver()).checkAllProductLinks()
                , "Some product links are broken");

    }

    @Severity(SeverityLevel.CRITICAL)
    @Story("Access the cart page from home page")
    @Description("Verify that clicking on the Cart button from the Home page navigates to the correct Cart URL")
    @Test(priority = 2)
    public void cliCkOnCartButton() {
        new HomePage(getDriver())
                .goToCart();
        Assert.assertTrue(new HomePage(getDriver())
                        .VerifyCartPageURL(DataUtils.getPropertyData("environments", "CART_URL")),
                "Cart page URL does not match expected URL.");
    }


}
