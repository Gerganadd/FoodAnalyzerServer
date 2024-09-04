package bg.sofia.uni.fmi.mjt.http;

import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpRespondFactoryTest {
    private static final Food CUCUMBER =
            new Food(2453490, "CUCUMBER HUMMUS, CUCUMBER", "020601412378");
    private static final Food RAFFAELLO_TREAT =
            new Food(2041155, "RAFFAELLO, ALMOND COCONUT TREAT", "009800146130");
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
    void testGetFoodByNameFromApiWithNullName() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi(null),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodByNameFromApiWithBlankName() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi(""),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi("  "),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodByNameFromApiWithInvalidName() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi("tom@toes"),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi("tom, toes"),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi("tom; toes 56"),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodByNameFromApiWithUnknownName() {
        assertThrows(NoSuchElementException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi("sunka"),
                "Expected NoSuchElementException but wasn't thrown");
        assertThrows(NoSuchElementException.class,
                () -> HttpRespondFactory.getFoodsByNameFromApi("rafaello"), // correct is: raffaello
                "Expected NoSuchElementException but wasn't thrown");
    }

    @Test
    void testGetFoodByNameFromApiWithValidName() throws NoSuchElementException {
        Foods expected = new Foods(List.of(RAFFAELLO_TREAT));
        Foods actual = HttpRespondFactory.getFoodsByNameFromApi("raffaello treat");

        Assertions.assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
    }

    @Test
    void testGetFoodReportFromApiWithNullCode()  {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodReportByFdcIdFromApi(null),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodReportFromApiWithBlankCode()  {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodReportByFdcIdFromApi(""),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodReportByFdcIdFromApi("   "),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodReportFromApiWithInvalidCode()  {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodReportByFdcIdFromApi("1234@34"),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodReportByFdcIdFromApi("1234 ,34"),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodReportByFdcIdFromApi(";123434"),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodReportFromApiWithUnknownCode()  {
        assertThrows(NoSuchElementException.class,
                () -> HttpRespondFactory.getFoodReportByFdcIdFromApi("1234"),
                "Expected NoSuchElementException but wasn't thrown");
    }

    @Test
    void testGetFoodReportFromApiWithValidCode() throws NoSuchElementException {
        String fcdId = String.valueOf(TOMATOES_REPORT.fdcId());
        FoodReport actual = HttpRespondFactory.getFoodReportByFdcIdFromApi(fcdId);

        Assertions.assertEquals(TOMATOES_REPORT, actual,
                "Expected: " + TOMATOES_REPORT + " but it was: " + actual);
    }

    @Test
    void testGetFoodByBarcodeFromApiWithNullCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodByBarcodeFromApi(null),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodByBarcodeFromApiWithBlankCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodByBarcodeFromApi(""),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodByBarcodeFromApi("   "),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodByBarcodeFromApiWithInvalidCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodByBarcodeFromApi("980abc98"),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodByBarcodeFromApi("98098abc"),
                "Expected IllegalArgumentException but wasn't thrown");
        assertThrows(IllegalArgumentException.class,
                () -> HttpRespondFactory.getFoodByBarcodeFromApi("abc98098"),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testGetFoodByBarcodeFromApiWithUnknownCode() {
        assertThrows(NoSuchElementException.class,
                () -> HttpRespondFactory.getFoodByBarcodeFromApi("12345"),
                "Expected NoSuchElementException but wasn't thrown");
    }

    @Test
    void testGetFoodByBarcodeFromApiWithValidCode() throws NoSuchElementException {
        Food actual = HttpRespondFactory.getFoodByBarcodeFromApi(CUCUMBER.gtinUpc());

        Assertions.assertEquals(CUCUMBER, actual,
                "Expected: " + CUCUMBER + " but it was: " + actual);
    }
}
