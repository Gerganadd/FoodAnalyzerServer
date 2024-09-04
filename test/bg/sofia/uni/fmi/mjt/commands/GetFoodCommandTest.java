package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.Food;
import bg.sofia.uni.fmi.mjt.foods.Foods;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetFoodCommandTest {
    private static final Foods GREEK_MOUSSAKA = new Foods(List.of(
            new Food(700884,
                    "GREEK MOUSSAKA MEATLESS MEATBALLS, GREEK MOUSSAKA",
                    "628025017508"),
            new Food(2643278,
                    "MOUSSAKA GREEK CASSEROLE WITH CREAMY BECHAMEL SAUCE, MOUSSAKA WITH CREAMY BECHAMEL SAUCE",
                    "860004129806")
    ));
    private static final Food RAFFAELLO_TREAT = new Food(
            2041155, "RAFFAELLO, ALMOND COCONUT TREAT", "009800146130");

    @Test
    void testExecuteWithInvalidAttribute() {
        Command command = new GetFoodCommand(List.of("raffaello", "@", "17treat"));

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testExecuteWithNoMatch() {
        Command command = new GetFoodCommand(List.of("sunka"));

        Assertions.assertThrows(NoSuchElementException.class,
                command::execute,
                "Expected NoSuchElementException but wasn't thrown");
    }

    @Test
    void testExecuteWithSingleMatch() throws NoSuchElementException {
        Command command = new GetFoodCommand(List.of("raffaello", "treat"));

        String expected = RAFFAELLO_TREAT.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown"); //?
    }

    @Test
    void testExecuteWithMultipleMatches() throws NoSuchElementException {
        Command command = new GetFoodCommand(List.of("greek", "moussaka"));

        String expected = GREEK_MOUSSAKA.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown"); //?
    }
}
