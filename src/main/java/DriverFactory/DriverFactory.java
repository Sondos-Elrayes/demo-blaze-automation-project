package DriverFactory;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;

public class DriverFactory {
    //for parallel execution
    private static final ThreadLocal<WebDriver> driverThreadLocal = new ThreadLocal<>();

    public static void setupDriver(String Browser) {

        if (driverThreadLocal.get() == null) {
            switch (Browser.toLowerCase()) {
                case "chrome":
                    ChromeOptions chromeOptions = new ChromeOptions();
                    //chromeOptions.addArguments("--headless");
                    chromeOptions.addArguments("--start-maximized");
                    driverThreadLocal.set(new ChromeDriver(chromeOptions));
                    break;
                case "firefox":
                    FirefoxOptions firefoxOptions = new FirefoxOptions();
                    //firefoxOptions.addArguments("-headless");
                    firefoxOptions.addArguments("--start-maximized");
                    driverThreadLocal.set(new FirefoxDriver(firefoxOptions));
                    break;
                default:
                    throw new IllegalArgumentException("Browser not supported: " + Browser);

            }
        }

    }

    public static WebDriver getDriver() {
        WebDriver driver = driverThreadLocal.get();
        if (driver == null) {
            throw new IllegalStateException("Driver not initialized. Call setupDriver() first.");
        }
        return driver;
    }

    public static void quitDriver() {
        if (driverThreadLocal.get() != null) {
            getDriver().quit();
            driverThreadLocal.remove();
        }
    }


}
