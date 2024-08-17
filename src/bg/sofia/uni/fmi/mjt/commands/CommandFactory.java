package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.logs.Logger;
import bg.sofia.uni.fmi.mjt.logs.Status;
import bg.sofia.uni.fmi.mjt.regexs.Regex;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class CommandFactory {
    private static final int COMMAND_NAME_INDEX = 0;
    private static final int COMMAND_NAME = 1;

    private static final String GET_FOOD_REGEX =
            CommandType.GET_FOOD.getText() + Regex.MATCH_SPACE + Regex.MATCH_ONLY_WORDS;
    private static final String GET_FOOD_REPORT_REGEX =
            CommandType.GET_FOOD_REPORT.getText() + Regex.MATCH_SPACE + Regex.MATCH_ONLY_DIGITS;
    private static final String GET_FOOD_BY_BARCODE_REGEX =
            CommandType.GET_FOOD_BY_BARCODE.getText() + Regex.MATCH_IMAGE_AND_BARCODE_ATTRIBUTES;

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
            //Logger.getInstance().addException(Status.UNKNOWN_COMMAND, "Command can't be null or blank");
            throw new UnknownCommandException("Command can't be null or blank");
        }

        Pattern patternForGetFood = Pattern.compile(GET_FOOD_REGEX);
        Pattern patternForGetFoodReport = Pattern.compile(GET_FOOD_REPORT_REGEX);
        Pattern patternForGetFoodByBarcode = Pattern.compile(GET_FOOD_BY_BARCODE_REGEX);

        Matcher matchGetFood = patternForGetFood.matcher(text);
        Matcher matchGetFoodReport = patternForGetFoodReport.matcher(text);
        Matcher matchGetFoodByBarcode = patternForGetFoodByBarcode.matcher(text);

        boolean res1 = matchGetFood.find();
        boolean res2 = matchGetFoodReport.find();
        boolean res3 = matchGetFoodByBarcode.find();

        if (!res1 && !res2 && !res3) {
            //Logger.getInstance().addLog(Status.UNKNOWN_COMMAND, text);
            throw new UnknownCommandException("Unsupported command : " + text);
        }
    }

    private static CommandType parseType(String text) throws UnknownCommandException {
        String[] args = text.split(Regex.MATCH_SPACE);
        String commandName = args[COMMAND_NAME_INDEX].trim();

        try {
            return CommandType.getValueOf(commandName);
        } catch (IllegalArgumentException e) {
            //Logger.getInstance().addLog(Status.UNKNOWN_COMMAND, "Unsupported command name: " + commandName);
            throw new UnknownCommandException("Unsupported command name: " + commandName, e);
        }
    }

    private static List<String> parseCommandAttributes(String text) {
        return Arrays
                .stream(text.split(Regex.MATCH_SPACE))
                .skip(COMMAND_NAME).
                collect(Collectors.toList());
    }
}
