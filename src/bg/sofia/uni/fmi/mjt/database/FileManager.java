package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.exceptions.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.logs.Logger;
import bg.sofia.uni.fmi.mjt.logs.Status;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileManager {
    private static final String SEPARATOR = ":";
    private static final String ROW_FORMAT = "%s" + SEPARATOR + "%s\n";
    private static final int KEY_INDEX = 0;
    private static final int VALUE_INDEX = 1;

    public static void saveTo(List<String> info, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (String row : info) {
                writer.write(row);
            }
        } catch (IOException e) {
            String message = ExceptionMessages.PROBLEM_WITH_WRITING_IN_FILE + filename;
            Logger.getInstance().addException(Status.UNABLE_TO_WRITE, message, e);
        }
    }

    public static void saveTo(Map<String, String> info, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry entry : info.entrySet()) {
                writer.write(String.format(ROW_FORMAT, entry.getKey(), entry.getValue()));
            }
        } catch (IOException e) {
            String message = ExceptionMessages.PROBLEM_WITH_WRITING_IN_FILE + filename;
            Logger.getInstance().addException(Status.UNABLE_TO_WRITE, message, e);
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
        } catch (IOException e) {
            String message = ExceptionMessages.PROBLEM_WITH_READING_FROM_FILE + filename;
            Logger.getInstance().addException(Status.UNABLE_TO_READ, message, e);
        }

        return result;
    }
}
