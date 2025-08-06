package listeners;

import Utilities.LogsUtils;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestResult;

public class ITestMethodListeners implements ITestListener {

    public void onTestStart(ITestResult result) {
        LogsUtils.info("Test Case " +result.getName() + " started");
    }

    public void onTestSuccess(ITestResult result) {
        LogsUtils.info("Test Case " +result.getName() + " Passed");
    }

    public void onTestSkipped(ITestResult result) {

        LogsUtils.info("Test Case " +result.getName() + " Skipped");
    }


}
