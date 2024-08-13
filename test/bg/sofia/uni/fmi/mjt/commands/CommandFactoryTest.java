package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.UnknownCommandException;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

public class CommandFactoryTest {

    @Test
    void testCreateWithNull() {
        assertThrows(UnknownCommandException.class, () -> CommandFactory.create(null),
                "Expected UnknownCommandException but wasn't thrown");
    }

    @Test
    void testCreateWithEmptyText() {
        assertThrows(UnknownCommandException.class, () -> CommandFactory.create(""),
                "Expected UnknownCommandException but wasn't thrown");

        assertThrows(UnknownCommandException.class, () -> CommandFactory.create("  "),
                "Expected UnknownCommandException but wasn't thrown");
    }

    @Test
    void testCreateWithInvalidCommandName() {
        assertThrows(UnknownCommandException.class,
                () -> CommandFactory.create("get-foods pasta"), // the correct is: get-food
                "Expected UnknownCommandException but wasn't thrown");

        assertThrows(UnknownCommandException.class,
                () -> CommandFactory.create("get-foods-report 892873"), // the correct is: get-food-report
                "Expected UnknownCommandException but wasn't thrown");

        assertThrows(UnknownCommandException.class,
                () -> CommandFactory.create("get-food-barcode --code=8293"), // the correct is: get-food-by-barcode
                "Expected UnknownCommandException but wasn't thrown");
    }

    @Test
    void testCreateWithValidGetFoodCommand() throws UnknownCommandException {
        Command actual1 = CommandFactory.create("get-food beef noodle soup");
        Command actual2 = CommandFactory.create("get-food tomatoes");

        GetFoodCommand expected1 = new GetFoodCommand(List.of("beef", "noodle", "soup"));
        GetFoodCommand expected2 = new GetFoodCommand(List.of("tomatoes"));

        assertInstanceOf(GetFoodCommand.class, actual1);
        assertInstanceOf(GetFoodCommand.class, actual2);

        assertIterableEquals(expected1.getAttributes(), actual1.getAttributes());
        assertIterableEquals(expected2.getAttributes(), actual2.getAttributes());
    }

    @Test
    void testCreateWithValidGetFoodReportCommand() throws UnknownCommandException {
        Command actual1 = CommandFactory.create("get-food-report 415269");
        Command actual2 = CommandFactory.create("get-food-report 732872");

        GetFoodCommand expected1 = new GetFoodCommand(List.of("415269"));
        GetFoodCommand expected2 = new GetFoodCommand(List.of("732872"));

        assertInstanceOf(GetFoodReportCommand.class, actual1);
        assertInstanceOf(GetFoodReportCommand.class, actual2);

        assertIterableEquals(expected1.getAttributes(), actual1.getAttributes());
        assertIterableEquals(expected2.getAttributes(), actual2.getAttributes());
    }

    @Test
    void testCreateWithValidGetFoodByBarcodeCommand() throws UnknownCommandException {
        Command actual1 = CommandFactory.create("get-food-by-barcode --img=D:\\Photos\\BarcodeImage.jpg --code=009800146130");
        Command actual2 = CommandFactory.create("get-food-by-barcode --img=D:\\Photos\\BarcodeImage.jpg");
        Command actual3 = CommandFactory.create("get-food-by-barcode --code=009800146130");

        GetFoodCommand expected1 = new GetFoodCommand(List.of("--img=D:\\Photos\\BarcodeImage.jpg", "--code=009800146130"));
        GetFoodCommand expected2 = new GetFoodCommand(List.of("--img=D:\\Photos\\BarcodeImage.jpg"));
        GetFoodCommand expected3 = new GetFoodCommand(List.of("--code=009800146130"));

        assertInstanceOf(GetFoodByBarcodeCommand.class, actual1);
        assertInstanceOf(GetFoodByBarcodeCommand.class, actual2);
        assertInstanceOf(GetFoodByBarcodeCommand.class, actual3);

        assertIterableEquals(expected1.getAttributes(), actual1.getAttributes());
        assertIterableEquals(expected2.getAttributes(), actual2.getAttributes());
        assertIterableEquals(expected3.getAttributes(), actual3.getAttributes());
    }
}
