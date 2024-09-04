package bg.sofia.uni.fmi.mjt.commands;

import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.Food;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class GetFoodByBarcodeCommandTest {
    private static final Food HONEY_CRISP = new Food(
            2543241, "HONEYCRISP APPLE ORGANIC CIDER, HONEYCRISP APPLE", "840600117766");
    private static final Food TOMATOES = new Food(
            2126333, "TOMATOES", "725439993357");

    private static final String TOMATOES_BARCODE_RELATIVE_PATH =
            "codes/TomatoesBarcode2.gif"; // to-do make it with Path
    private static final String TOMATOES_QR_Code_RELATIVE_PATH =
            "codes/TomatoesQRcode2.gif"; // to-do make it with Path
    private static final String INVALID_IMAGE_PATH = "codes/something.png";
    private static final String NO_CODE_IMAGE_PATH = "codes/NoCodeImage.png";

    @Test
    void testExecuteWithBlankImageAttribute() {
        String image = "--img=  ";

        GetFoodByBarcodeCommand command =
                new GetFoodByBarcodeCommand(List.of(image));

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testExecuteWithInvalidImagePath() {
        String image = String.format("--img=%s", INVALID_IMAGE_PATH);

        GetFoodByBarcodeCommand command =
                new GetFoodByBarcodeCommand(List.of(image));

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testExecuteWithImageThatDoesNotContainsCode() {
        String image = String.format("--img=%s", NO_CODE_IMAGE_PATH);

        GetFoodByBarcodeCommand command =
                new GetFoodByBarcodeCommand(List.of(image));

        Assertions.assertThrows(IllegalArgumentException.class,
                command::execute,
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testExecuteWithOnlyCodeAttribute() throws NoSuchElementException {
        String code = String.format("--code=%s", HONEY_CRISP.gtinUpc());
        GetFoodByBarcodeCommand command = new GetFoodByBarcodeCommand(List.of(code));

        String expected = HONEY_CRISP.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown");
    }

    @Test
    void testExecuteWithOnlyBarcodeImagePathAttribute() throws NoSuchElementException {
        String image = String.format("--img=%s", TOMATOES_BARCODE_RELATIVE_PATH);

        GetFoodByBarcodeCommand command =
                new GetFoodByBarcodeCommand(List.of(image));

        String expected = TOMATOES.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown");
    }

    @Test
    void testExecuteWithOnlyQRCodeImagePathAttribute() throws NoSuchElementException {
        String image = String.format("--img=%s", TOMATOES_QR_Code_RELATIVE_PATH);

        GetFoodByBarcodeCommand command =
                new GetFoodByBarcodeCommand(List.of(image));

        String expected = TOMATOES.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown");
    }

    @Test
    void testExecuteWithCodeAndImageAttributes() throws NoSuchElementException {
        String code = String.format("--code=%s", TOMATOES.gtinUpc());
        String image = String.format("--img=%s", TOMATOES_QR_Code_RELATIVE_PATH);
        GetFoodByBarcodeCommand command = new GetFoodByBarcodeCommand(List.of(code, image));

        String expected = TOMATOES.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown");
    }

    @Test
    void testExecuteWithCodeAndImageAttributesSkipImageAttribute() throws NoSuchElementException {
        String code = String.format("--code=%s", HONEY_CRISP.gtinUpc());
        String image = String.format("--img=%s", TOMATOES_BARCODE_RELATIVE_PATH); // different gtinUpc
        GetFoodByBarcodeCommand command = new GetFoodByBarcodeCommand(List.of(code, image));

        String expected = HONEY_CRISP.toString();
        String actual = command.execute();

        assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
        assertDoesNotThrow(command::execute,
                "Expected to NoSuchElementException to be catch but it was thrown");
    }
}
