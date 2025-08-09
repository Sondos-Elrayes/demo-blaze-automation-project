package Utilities;

import com.google.gson.*;
import com.google.gson.reflect.TypeToken;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Map;
import java.util.Properties;

public class DataUtils {
    public static final String TEST_DATA_PATH = "src/test/resources/TestData/";

    //reading data from JSON file
    public static String getJsonData(String fileName, String key) {
        try {
            FileReader fileReader = new FileReader(TEST_DATA_PATH + fileName + ".json");
            JsonElement jsonElement = JsonParser.parseReader(fileReader);
            if (jsonElement.isJsonObject()) {
                return jsonElement.getAsJsonObject().get(key).getAsString();
            } else {
                throw new IllegalArgumentException("JSON file does not contain a valid object.");
            }

        } catch (FileNotFoundException e) {
            throw new RuntimeException("JSON file not found: " + fileName, e);
        }
    }

    public static String getJsonDataFromList(String fileName, String key) {
        try {
            JsonElement fileElement = JsonParser.parseReader(
                    new FileReader("src/test/resources/TestData/" + fileName + ".json")
            );
            JsonObject jsonObject = fileElement.getAsJsonObject();

            if (key.contains("[")) {
                String arrayKey = key.substring(0, key.indexOf("["));
                int index = Integer.parseInt(key.replaceAll("[^0-9]", ""));
                JsonArray jsonArray = jsonObject.getAsJsonArray(arrayKey);
                return jsonArray.get(index).getAsString();
            } else {
                JsonElement value = jsonObject.get(key);
                if (value == null) {
                    throw new IllegalArgumentException("Key not found in JSON: " + key);
                }
                return value.getAsString();
            }

        } catch (Exception e) {
            throw new RuntimeException("Failed to read JSON data: " + e.getMessage(), e);
        }
    }


    //reading data from properties file
    public static String getPropertyData(String fileName, String key) {
        Properties properties = new Properties();
        try {
            properties.load(new FileReader(TEST_DATA_PATH + fileName + ".properties"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to load properties file: " + fileName, e);
        }
        return properties.getProperty(key);
    }

    //reading JSON data as a Map to override the need for multiple getJsonData calls
    public static Map<String, String> getJsonDataAsMap(String filePath) {
        try (Reader reader = new FileReader(filePath)) {
            return new Gson().fromJson(reader, new TypeToken<Map<String, String>>() {
            }.getType());
        } catch (IOException e) {
            throw new RuntimeException("Failed to read JSON file: " + filePath, e);
        }
    }
}
