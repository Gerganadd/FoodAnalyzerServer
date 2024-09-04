package bg.sofia.uni.fmi.mjt.foods;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodReportTest {
    private static final Nutrient FAT = new Nutrient(0.0);
    private static final Nutrient FIBER = new Nutrient(1.03);
    private static final Nutrient SUGARS = new Nutrient(3.0);
    private static final Nutrient PROTEIN = new Nutrient(1.0);
    private static final Nutrient CALORIES = new Nutrient(25.1);
    private static final NutrientsReport NUTRIENT_REPORT = new NutrientsReport(
            FAT, FIBER, SUGARS, PROTEIN, CALORIES);
    private static final FoodReport FOOD_REPORT = new FoodReport(
            2543214, "TOMATOES",
            "SAN MARZANO PEELED TOMATOES, BASIL LEAF.",
            NUTRIENT_REPORT);
    private static final String SERIALIZED_FOOD_REPORT =
            "2543214@TOMATOES@SAN MARZANO PEELED TOMATOES, BASIL LEAF.@0.0#1.03#3.0#1.0#25.1";

    @Test
    void testSerializeIsCorrect() {
        String actual = FOOD_REPORT.serialize();

        assertEquals(SERIALIZED_FOOD_REPORT, actual,
                "Expected: " + SERIALIZED_FOOD_REPORT + " but it was: " + actual);
    }

    @Test
    void testDeserializeIsCorrect() {
        FoodReport actual = FoodReport.deserialize(SERIALIZED_FOOD_REPORT);

        assertEquals(FOOD_REPORT, actual,
                "Expected: " + FOOD_REPORT + " but it was: " + actual);
    }
}
