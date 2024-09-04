package bg.sofia.uni.fmi.mjt.foods;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodTest {
    private static final Food FOOD_WITH_NULL_VALUE = new Food(
            700884, "GREEK MOUSSAKA, MEATLESS MEATBALLS", null);
    private static final Food FOOD_WITH_STRING_NULL_VALUE = new Food(
            700884, "GREEK MOUSSAKA, MEATLESS MEATBALLS", "null");
    private static final Food FOOD_WITH_ALL_VALUES = new Food(
            700884, "GREEK MOUSSAKA, MEATLESS MEATBALLS", "628025017508");
    private static final String SERIALIZED_WITH_ALL_VALUES =
            "700884@GREEK MOUSSAKA, MEATLESS MEATBALLS@628025017508";
    private static final String SERIALIZED_WITH_NULL_VALUE =
            "700884@GREEK MOUSSAKA, MEATLESS MEATBALLS@null";

    @Test
    void testSerializeIsCorrect() {
        String actualWithAllValues = FOOD_WITH_ALL_VALUES.serialize();
        String actualWithNullValue = FOOD_WITH_NULL_VALUE.serialize();
        String actualWithStringNullValue = FOOD_WITH_STRING_NULL_VALUE.serialize();

        assertEquals(SERIALIZED_WITH_ALL_VALUES, actualWithAllValues,
                "Expected: " + SERIALIZED_WITH_ALL_VALUES +
                        " but it was: " + actualWithAllValues);

        assertEquals(SERIALIZED_WITH_NULL_VALUE, actualWithNullValue,
                "Expected: " + SERIALIZED_WITH_ALL_VALUES +
                        " but it was: " + actualWithNullValue);

        assertEquals(SERIALIZED_WITH_NULL_VALUE, actualWithStringNullValue,
                "Expected: " + SERIALIZED_WITH_ALL_VALUES +
                        " but it was: " + actualWithStringNullValue);
    }

    @Test
    void testDeserializeIsCorrect() {
        Food actualWithAllValues = Food.deserialize(SERIALIZED_WITH_ALL_VALUES);
        Food actualWithNullValue = Food.deserialize(SERIALIZED_WITH_NULL_VALUE);

        assertEquals(FOOD_WITH_ALL_VALUES, actualWithAllValues,
                "Expected: " + FOOD_WITH_ALL_VALUES +
                        " but it was: " + actualWithAllValues);

        assertEquals(FOOD_WITH_NULL_VALUE, actualWithNullValue,
                "Expected: " + FOOD_WITH_NULL_VALUE +
                        " but it was: " + actualWithNullValue);
    }
}
