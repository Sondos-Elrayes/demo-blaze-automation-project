package Utilities;

import org.apache.logging.log4j.LogManager;

import java.io.File;
import java.util.Arrays;
import java.util.Comparator;


public class LogsUtils {
    public static final String LOGS_PATH = "test-outputs/Logs";

    //getListLogFile
    public static File getListFile(String path) {
        File folder = new File(path);
        File[] files = folder.listFiles();
        assert files != null;
        if (files.length == 0) {
            return null;
        }
        Arrays.sort(files, Comparator.comparingLong(File::lastModified).reversed());
        return files[0];

    }

    public static void trace(String message) {
        LogManager.getLogger(Thread.currentThread().getStackTrace()[2].toString())
                .trace(message);
    }

    public static void debug(String message) {
        LogManager.getLogger(Thread.currentThread().getStackTrace()[2].toString())
                .debug(message);
    }

    public static void info(String message) {
        LogManager.getLogger(Thread.currentThread().getStackTrace()[2].toString())
                .info(message);
    }

    public static void warn(String message) {
        LogManager.getLogger(Thread.currentThread().getStackTrace()[2].toString())
                .warn(message);
    }

    public static void error(String message) {
        LogManager.getLogger(Thread.currentThread().getStackTrace()[2].toString())
                .error(message);
    }

    public static void fatal(String message) {
        LogManager.getLogger(Thread.currentThread().getStackTrace()[2].toString())
                .fatal(message);
    }

}
