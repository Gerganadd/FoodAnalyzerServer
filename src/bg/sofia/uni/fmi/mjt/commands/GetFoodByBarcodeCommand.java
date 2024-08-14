package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.foods.Food;
import bg.sofia.uni.fmi.mjt.regexs.Regex;

import java.util.List;

public class GetFoodByBarcodeCommand extends Command {
    private static final String IMAGE_ATTRIBUTE_NAME = "--img";
    private static final String BARCODE_ATTRIBUTE_NAME = "--code";
    private static final int VALUE_INDEX = 1;

    private String barcode;
    private String imagePath;
    public GetFoodByBarcodeCommand(List<String> attributes) {
        super(attributes);

        parseCode(attributes);
        parseImage(attributes);
    }

    //to-do write documentation
    @Override
    public String execute() {
        if (barcode == null) {
            parseBarcodeFromImage();
        }

        Food res = DatabaseManager.getInstance().getFoodByBarcode(barcode);
        return res.toString();
    }

    private void parseCode(List<String> attributes) {
        attributes
                .stream()
                .filter(x -> x.startsWith(BARCODE_ATTRIBUTE_NAME))
                .findFirst()
                .ifPresent(s -> barcode = s.split(Regex.MATCH_EQUALS_SIGN)[VALUE_INDEX]);
    }

    private void parseImage(List<String> attributes) {
        attributes
                .stream()
                .filter(x -> x.startsWith(IMAGE_ATTRIBUTE_NAME))
                .findFirst()
                .ifPresent(s -> imagePath = s.split(Regex.MATCH_EQUALS_SIGN)[VALUE_INDEX]);
    }

    private void parseBarcodeFromImage() {
        if (imagePath == null || imagePath.isBlank()) {
            throw new IllegalArgumentException("Invalid image path"); //? this type of exception
        }

        //barcode = ... (get barcode from image path) // to-do
    }
}
