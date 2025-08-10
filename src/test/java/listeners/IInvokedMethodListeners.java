package listeners;

import Utilities.AttachmentsUtils;
import Utilities.LogsUtils;
import io.qameta.allure.Allure;
import org.testng.IInvokedMethod;
import org.testng.IInvokedMethodListener;
import org.testng.ITestContext;
import org.testng.ITestResult;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static DriverFactory.DriverFactory.getDriver;

public class IInvokedMethodListeners implements IInvokedMethodListener {

    public void beforeInvocation(IInvokedMethod method, ITestResult testResult) {

    }

    public void afterInvocation(IInvokedMethod method, ITestResult testResult, ITestContext context) {
        File logFile = LogsUtils.getListFile(LogsUtils.LOGS_PATH);
        try {
            assert logFile != null;
            Allure.addAttachment("logs.log", Files.readString(Path.of(logFile.getPath())));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (testResult.getStatus() == ITestResult.FAILURE) {
            LogsUtils.info("Test Case " + testResult.getName() + " failed with exception: " + testResult.getThrowable());
            AttachmentsUtils.takeScreenShot(getDriver(), testResult.getName());
            //TODO  //AttachmentsUtils.takeFullScreenshot(getDriver(), B);
        }

    }
}
