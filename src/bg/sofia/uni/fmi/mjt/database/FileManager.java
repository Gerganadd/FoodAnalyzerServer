package bg.sofia.uni.fmi.mjt.database;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.Map;

public class FileManager {
    private static final String SEPARATOR = ":";
    private static final String ROW_FORMAT = "%s" + SEPARATOR + "%s" + "\n";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    public static void saveTo(Map<String, String> info, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry entry : info.entrySet()) {
                writer.write(String.format(ROW_FORMAT, entry.getKey(), entry.getValue()));
            }

            System.out.println("Successfully wrote to the file.");
        } catch (IOException e) {
            e.printStackTrace(); // to-do save the exception in exception file
        }
    }

    public static Map<String, String> loadFrom(String filename) {
        Map<String, String> result = new HashMap<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.lines().forEach(x -> {
                String[] args = x.split(SEPARATOR);
                String key = args[KEY_INDEX].trim();
                String value = args[VALUE_INDEX].trim();

                result.put(key, value);
            });

            System.out.println("Successfully read the file.");
        } catch (IOException e) {
            e.printStackTrace(); // to-do save the exception in exception file
        }

        return result;
    }
}
