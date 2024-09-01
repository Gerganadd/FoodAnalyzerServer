package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.logs.Logger;
import bg.sofia.uni.fmi.mjt.logs.Status;

import java.io.FileWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.io.IOException;

import java.util.ArrayList;
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

            String message = "Successfully wrote to the file=" + filename;
            Logger.getInstance().addLog(Status.SUCCESSFUL_WRITE_IN_FILE, message);
        } catch (IOException e) {
            String message = "IO exception occurred with writhing the file=" + filename;
            Logger.getInstance().addException(Status.UNABLE_TO_WRITE, message, e);

            System.out.println("Problem with saving the info in file " + filename);
        }
    }

    public static void saveTo(Map<String, String> info, String filename) {
        try (FileWriter writer = new FileWriter(filename)) {
            for (Map.Entry entry : info.entrySet()) {
                writer.write(String.format(ROW_FORMAT, entry.getKey(), entry.getValue()));
            }

            String message = "Successfully wrote to the file=" + filename;
            Logger.getInstance().addLog(Status.SUCCESSFUL_WRITE_IN_FILE, message);
        } catch (IOException e) {
            String message = "IO exception occurred with writhing the file=" + filename;
            Logger.getInstance().addException(Status.UNABLE_TO_WRITE, message, e);

            System.out.println("Problem with saving the info in file " + filename);
        }
    }

    public static List<String> loadLinesFrom(String filename) {
        List<String> result = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new FileReader(filename))) {
            reader.lines().forEach(result::add);

            String message = "Successfully read from the file=" + filename;
            Logger.getInstance().addLog(Status.SUCCESSFUL_READ_FROM_FILE, message);
        } catch (IOException e) {
            String message = "IO exception occurred with reading from the file=" + filename;
            Logger.getInstance().addException(Status.UNABLE_TO_READ, message, e);

            System.out.println("Problem with loading the info from file " + filename);
        }

        return result;
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

            String message = "Successfully read from the file=" + filename;
            Logger.getInstance().addLog(Status.SUCCESSFUL_READ_FROM_FILE, message);
        } catch (IOException e) {
            String message = "IO exception occurred with reading from the file=" + filename;
            Logger.getInstance().addException(Status.UNABLE_TO_READ, message, e);

            System.out.println("Problem with loading the info from file " + filename);
        }

        return result;
    }
}
