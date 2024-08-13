package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.foods.Food;
import bg.sofia.uni.fmi.mjt.foods.FoodReport;
import bg.sofia.uni.fmi.mjt.foods.Foods;

import bg.sofia.uni.fmi.mjt.http.HttpRespondFactory;

import java.util.HashMap;
import java.util.Map;

public class DatabaseManager {
    private static final String DEFAULT_FOODS_BY_NAME_FILE_NAME = "foodsByName.txt";
    private static final String DEFAULT_FOOD_REPORT_FILE_NAME = "food-reports.txt";
    private static final String DEFAULT_FOODS_BY_BARCODE_FILE_NAME = "foodsByBarcode.txt";

    private static DatabaseManager manager;

    private Map<String, Foods> foodsByName = new HashMap<>(); // to-do: read it from file
    private Map<Long, FoodReport> foodReports = new HashMap<>(); // to-do: read it from file
    private Map<String, Food> foodsByBarcode = new HashMap<>(); // to-do: read it from file

    private DatabaseManager() {
        //to-do load info from files
    }

    public static DatabaseManager getInstance() {
        if (manager == null) {
            manager = new DatabaseManager();
        }
        return manager;
    }

    public Foods getFoodsByName(String name) {
        if (foodsByName.containsKey(name)) {
            return foodsByName.get(name);
        }

        Foods result = HttpRespondFactory.getFoodsByNameFromApi(name);

        foodsByName.put(name, result);

        return result;
    }

    public FoodReport getFoodReportBy(long fdcId) {
        if (foodReports.containsKey(fdcId)) {
            return foodReports.get(fdcId);
        }

        String id = Long.toString(fdcId);
        FoodReport result = HttpRespondFactory.getFoodReportByFdcIdFromApi(id);

        foodReports.put(fdcId, result);

        return result;
    }

    public Food getFoodByBarcode(String gtinUpc) {
        if (foodsByBarcode.containsKey(gtinUpc)) {
            return foodsByBarcode.get(gtinUpc);
        }

        Food result = HttpRespondFactory.getFoodByBarcodeFromApi(gtinUpc);

        foodsByBarcode.put(gtinUpc, result);

        return result;
    }
}
