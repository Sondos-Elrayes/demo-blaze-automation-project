package listeners;

import Utilities.AttachmentsUtils;
import Utilities.LogsUtils;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

import static DriverFactory.DriverFactory.getDriver;

public class IInvokedMethodListeners implements IInvokedMethodListener {

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

        public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
            if (testResult.getStatus() == ITestResult.FAILURE) {
                LogsUtils.info("Test Case " + testResult.getName() + " failed with exception: " + testResult.getThrowable());
                AttachmentsUtils.takeScreenShot(getDriver(), testResult.getName());
            }

    }
}
