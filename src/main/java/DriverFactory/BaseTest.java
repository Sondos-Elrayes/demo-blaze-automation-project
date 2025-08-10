package DriverFactory;

import Utilities.DataUtils;
import Utilities.LogsUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;

import java.time.Duration;

import static DriverFactory.DriverFactory.quitDriver;
import static DriverFactory.DriverFactory.setupDriver;

public abstract class BaseTest {

    protected WebDriver getDriver() {
        return DriverFactory.getDriver();
    }

    @BeforeMethod(alwaysRun = true)
    public void setup() {
        ChromeOptions chromeOptions = new ChromeOptions();
        chromeOptions.addArguments("--headless");
        setupDriver(DataUtils.getPropertyData("environments", "BROWSER"));
        LogsUtils.info("chrome driver is set up successfully.");
        getDriver().get(DataUtils.getPropertyData("environments", "LAUNCH_URL"));
        LogsUtils.info("Application URL is launched successfully.");
        getDriver().manage().timeouts()
                .implicitlyWait(Duration.ofSeconds(5));
    }

//    public void setup() {
//        boolean headless = Boolean.parseBoolean(System.getProperty("headless", "true"));
//
//        ChromeOptions options = new ChromeOptions();
//        if (headless) {
//            options.addArguments("--headless=new", "--window-size=1920,1080");
//            // optional but helpful:
//            options.addArguments("--disable-gpu", "--no-sandbox", "--disable-dev-shm-usage");
//        }
//
//        setupDriver("chrome", options);   // <-- pass options
//        LogsUtils.info("chrome driver is set up successfully.");
//
//        getDriver().get(DataUtils.getPropertyData("environments", "LAUNCH_URL"));
//        getDriver().manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
//    }

    @AfterMethod(alwaysRun = true)
    public void tearDown() {
        quitDriver();
    }
}
