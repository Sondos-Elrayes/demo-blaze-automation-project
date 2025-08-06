package Utilities;

import com.google.gson.JsonElement;
import com.google.gson.JsonParser;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Properties;

public class DataUtils {
    public static final String TEST_DATA_PATH = "src/test/resources/TestData/";
    //reading data from JSON file
    public static String getJsonData(String fileName ,String key) throws FileNotFoundException {
        FileReader fileReader = new FileReader(TEST_DATA_PATH + fileName + ".json");
        JsonElement jsonElement = JsonParser.parseReader(fileReader);
        if (jsonElement.isJsonObject()) {
            return jsonElement.getAsJsonObject().get(key).getAsString();
        } else {
            throw new IllegalArgumentException("JSON file does not contain a valid object.");
        }
    }

    //reading data from properties file
    public static String getPropertyData(String fileName, String key)   {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader( TEST_DATA_PATH+ fileName + ".properties"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file: " + fileName, e);
        }
        return properties.getProperty(key);
    }
}
