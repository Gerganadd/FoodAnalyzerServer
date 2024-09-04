package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.FoodReport;
import bg.sofia.uni.fmi.mjt.foods.Nutrient;
import bg.sofia.uni.fmi.mjt.foods.NutrientsReport;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetFoodReportCommandTest {
    private static final Nutrient TOMATOES_FAT = new Nutrient(0.0);
    private static final Nutrient TOMATOES_FIBER = new Nutrient(1.03);
    private static final Nutrient TOMATOES_SUGARS = new Nutrient(3.0);
    private static final Nutrient TOMATOES_PROTEIN = new Nutrient(1.0);
    private static final Nutrient TOMATOES_CALORIES = new Nutrient(25.1);
    private static final NutrientsReport TOMATOES_NUTRIENT_REPORT = new NutrientsReport(
            TOMATOES_FAT, TOMATOES_FIBER, TOMATOES_SUGARS, TOMATOES_PROTEIN, TOMATOES_CALORIES);
    private static final FoodReport TOMATOES_REPORT = new FoodReport(
            2543214, "TOMATOES",
            "SAN MARZANO PEELED TOMATOES, SAN MARZANO TOMATO PUREE, BASIL LEAF.",
            TOMATOES_NUTRIENT_REPORT);

    @Test
    void testExecuteWithInvalidAttribute() {
        Command command = new GetFoodReportCommand(List.of("123jk23"));

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testExecuteWithNoMatch() {
        Command command = new GetFoodReportCommand(List.of("1234"));

        Assertions.assertThrows(NoSuchElementException.class,
                command::execute,
                "Expected NoSuchElementException but wasn't thrown");
    }

    @Test
    void testExecuteWithMatch() throws NoSuchElementException {
        Command command = new GetFoodReportCommand(List.of("2543214"));

        String expected = TOMATOES_REPORT.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown");
    }

}
