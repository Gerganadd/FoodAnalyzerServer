package bg.sofia.uni.fmi.mjt.database;

import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.Food;
import bg.sofia.uni.fmi.mjt.foods.FoodReport;
import bg.sofia.uni.fmi.mjt.foods.Foods;

import bg.sofia.uni.fmi.mjt.http.HttpRespondFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

public class DatabaseManager {
    private static final String DEFAULT_FOODS_BY_NAME_FILE_NAME = "foodsByName.txt";
    private static final String DEFAULT_FOOD_REPORT_FILE_NAME = "food-reports.txt";
    private static final String DEFAULT_FOODS_BY_BARCODE_FILE_NAME = "foodsByBarcode.txt";
    private static DatabaseManager manager;
    private Map<String, Foods> foodsByName = new HashMap<>();
    private Map<Long, FoodReport> foodReports = new HashMap<>();
    private Map<String, Food> foodsByBarcode = new HashMap<>();

    private DatabaseManager() {
        loadFoodsByName();
        loadFoodReports();
        loadFoodsByBarcode();
    }

    public static DatabaseManager getInstance() {
        if (manager == null) {
            manager = new DatabaseManager();
        }
        return manager;
    }

    private void loadFoodsByName() {
        Map<String, Foods> fileInfo = FileManager.loadFrom(DEFAULT_FOODS_BY_NAME_FILE_NAME)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, e2 -> Foods.deserialize(e2.getValue())));

        foodsByName.putAll(fileInfo);
    }

    private void loadFoodsByBarcode() {
        Map<String, Food> fileInfo = FileManager.loadFrom(DEFAULT_FOODS_BY_BARCODE_FILE_NAME)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        Map.Entry::getKey, e2 -> Food.deserialize(e2.getValue())));

        foodsByBarcode.putAll(fileInfo);
    }

    private void loadFoodReports() {
        Map<Long, FoodReport> fileInfo = FileManager.loadFrom(DEFAULT_FOOD_REPORT_FILE_NAME)
                .entrySet()
                .stream()
                .collect(Collectors.toMap(
                        e1 -> Long.parseLong(e1.getKey()), e2 -> FoodReport.deserialize(e2.getValue())));

        foodReports.putAll(fileInfo);
    }

    private void addAllFoodsWithGtinUpcCodeFromResultInDatabase(Foods result) {
        for (Food food : result.foods()) {
            String gtinUpcCode = food.gtinUpc();
            if (gtinUpcCode == null || gtinUpcCode.isBlank()) {
                continue;
            }

            if (!foodsByBarcode.containsKey(gtinUpcCode)) {
                foodsByBarcode.put(gtinUpcCode, food);
            }
        }
    }

    public Foods getFoodsByName(String name) throws NoSuchElementException {
        if (foodsByName.containsKey(name)) {
            return foodsByName.get(name);
        }

        Foods result = HttpRespondFactory.getFoodsByNameFromApi(name);

        addAllFoodsWithGtinUpcCodeFromResultInDatabase(result);

        foodsByName.put(name, result);

        return result;
    }

    public FoodReport getFoodReportBy(long fdcId) throws NoSuchElementException {
        if (foodReports.containsKey(fdcId)) {
            return foodReports.get(fdcId);
        }

        String id = Long.toString(fdcId);
        FoodReport result = HttpRespondFactory.getFoodReportByFdcIdFromApi(id);

        foodReports.put(fdcId, result);

        return result;
    }

    public Food getFoodByBarcode(String gtinUpc) throws NoSuchElementException {
        if (foodsByBarcode.containsKey(gtinUpc)) {
            return foodsByBarcode.get(gtinUpc);
        }

        Food result = HttpRespondFactory.getFoodByBarcodeFromApi(gtinUpc);

        foodsByBarcode.put(gtinUpc, result);

        return result;
    }

    public void saveData() {
        Map<String, String> foodsByNameData = foodsByName
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e2 -> e2.getValue().serialize()));

        Map<String, String> foodsByBarcodeData = foodsByBarcode
                .entrySet()
                .stream()
                .collect(Collectors.toMap(Map.Entry::getKey, e2 -> e2.getValue().serialize()));

        Map<String, String> foodReportsData = foodReports
                .entrySet()
                .stream()
                .collect(Collectors.toMap(e1 -> Long.toString(e1.getKey()), e2 -> e2.getValue().serialize()));

        FileManager.saveTo(foodsByNameData, DEFAULT_FOODS_BY_NAME_FILE_NAME);
        FileManager.saveTo(foodsByBarcodeData, DEFAULT_FOODS_BY_BARCODE_FILE_NAME);
        FileManager.saveTo(foodReportsData, DEFAULT_FOOD_REPORT_FILE_NAME);
    }
}
