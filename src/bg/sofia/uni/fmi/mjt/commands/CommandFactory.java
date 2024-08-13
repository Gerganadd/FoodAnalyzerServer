package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandFactory {
    private static final String GET_FOOD_REGEX =
            CommandType.GET_FOOD.getText() + " [a-zA-z ]+";
    private static final String GET_FOOD_REPORT_REGEX =
            CommandType.GET_FOOD_REPORT.getText() + " [0-9]+";
    private static final String GET_FOOD_BY_BARCODE_REGEX =
            CommandType.GET_FOOD_BY_BARCODE.getText() + "( --((img=[\\w:\\\\.]*)|(code=[0-9]+))){1,2}";

    public static Command create(String text) throws UnknownCommandException {
        validateText(text);

        CommandType type = parseType(text);
        List<String> attributes = parseCommandAttributes(text);

        return switch (type) {
            case GET_FOOD -> new GetFoodCommand(attributes);
            case GET_FOOD_REPORT -> new GetFoodReportCommand(attributes);
            case GET_FOOD_BY_BARCODE -> new GetFoodByBarcodeCommand(attributes);
        };
    }

    private static void validateText(String text) throws UnknownCommandException {
        if (text == null || text.isBlank()) {
            throw new UnknownCommandException("Command can't be null or blank");
        }

        Pattern patternForGetFood = Pattern.compile(GET_FOOD_REGEX);
        Pattern patternForGetFoodReport = Pattern.compile(GET_FOOD_REPORT_REGEX);
        Pattern patternForGetFoodByBarcode = Pattern.compile(GET_FOOD_BY_BARCODE_REGEX);

        Matcher matchGetFood = patternForGetFood.matcher(text);
        Matcher matchGetFoodReport = patternForGetFoodReport.matcher(text);
        Matcher matchGetFoodByBarcode = patternForGetFoodByBarcode.matcher(text);

        boolean r1 = matchGetFood.find();
        boolean r2 = matchGetFoodReport.find();
        boolean r3 = matchGetFoodByBarcode.find();

        if (!r1 && !r2 && !r3) {
            throw new UnknownCommandException("Unsupported command : " + text);
        }
    }

    private static CommandType parseType(String text) throws UnknownCommandException {
        String commandName = text.split(" ")[0].trim();

        try {
            return CommandType.getValueOf(commandName);
        } catch (IllegalArgumentException e) {
            throw new UnknownCommandException("Unsupported command name: " + commandName, e);
        }
    }

    private static List<String> parseCommandAttributes(String text) {
        return Arrays
                .stream(text.split(" "))
                .skip(1).
                collect(Collectors.toList());
    }
}
