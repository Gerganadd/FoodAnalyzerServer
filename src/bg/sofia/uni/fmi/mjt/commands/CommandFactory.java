package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;
import bg.sofia.uni.fmi.mjt.regexs.Regex;

import java.util.Arrays;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
            throw new UnknownCommandException(ExceptionMessages.COMMAND_NULL_OR_BLANK);
        }

        text = text.trim();

        Pattern patternForGetFood = Pattern.compile(GET_FOOD_REGEX);
        Pattern patternForGetFoodReport = Pattern.compile(GET_FOOD_REPORT_REGEX);
        Pattern patternForGetFoodByBarcode = Pattern.compile(GET_FOOD_BY_BARCODE_REGEX);

        Matcher matchGetFood = patternForGetFood.matcher(text);
        Matcher matchGetFoodReport = patternForGetFoodReport.matcher(text);
        Matcher matchGetFoodByBarcode = patternForGetFoodByBarcode.matcher(text);

        boolean res1 = matchGetFood.matches();
        boolean res2 = matchGetFoodReport.matches();
        boolean res3 = matchGetFoodByBarcode.matches();

        if (!res1 && !res2 && !res3) {
            String message = generateExceptionMessage(text);
            throw new UnknownCommandException(message);
        }
    }

    private static CommandType parseType(String text) throws UnknownCommandException {
        String commandName = getCommandName(text);

        try {
            return CommandType.getValueOf(commandName);
        } catch (IllegalArgumentException e) {
            throw new UnknownCommandException(
                    ExceptionMessages.UNKNOWN_COMMAND_TYPE + commandName, e);
        }
    }

    private static String getCommandName(String command) {
        return command
                .split(Regex.MATCH_SPACE)[COMMAND_NAME_INDEX]
                .trim();
    }

    private static List<String> parseCommandAttributes(String text) {
        return Arrays
                .stream(text.trim().split(Regex.MATCH_SPACE))
                .skip(COMMAND_NAME)
                .toList();
    }

    private static String generateExceptionMessage(String command) {
        try {
            CommandType commandType = parseType(command);

            String commandDescription = switch (commandType) {
                case GET_FOOD -> ExceptionMessages.GET_FOOD_COMMAND_DESCRIPTION;
                case GET_FOOD_REPORT -> ExceptionMessages.GET_FOOD_REPORT_COMMAND_DESCRIPTION;
                case GET_FOOD_BY_BARCODE -> ExceptionMessages.GET_FOOD_BY_BARCODE_COMMAND_DESCRIPTION;
            };

            return ExceptionMessages.INVALID_COMMAND_ATTRIBUTES + commandDescription;
        } catch (UnknownCommandException e) {
            return e.getMessage();
        }
    }
}
