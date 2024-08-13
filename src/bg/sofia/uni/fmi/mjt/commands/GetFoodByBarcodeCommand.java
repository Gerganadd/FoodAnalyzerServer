package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.database.DatabaseManager;
import bg.sofia.uni.fmi.mjt.foods.Food;

import java.util.List;

public class GetFoodByBarcodeCommand extends Command {
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
                .filter(x -> x.startsWith("--code"))
                .findFirst()
                .ifPresent(s -> barcode = s.split("=")[1]);
    }

    private void parseImage(List<String> attributes) {
        attributes
                .stream()
                .filter(x -> x.startsWith("--img"))
                .findFirst()
                .ifPresent(s -> imagePath = s.split("=")[1]);
    }

    private void parseBarcodeFromImage() {
        if (imagePath == null || imagePath.isBlank()) {
            throw new IllegalArgumentException("Invalid image path"); //? this type of exception
        }

        //barcode = ... (get barcode from image path) // to-do
    }
}
