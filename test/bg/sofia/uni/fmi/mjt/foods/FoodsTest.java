package bg.sofia.uni.fmi.mjt.foods;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FoodsTest {
    private static final Foods FOODS = new Foods(List.of(
            new Food(700884,
                    "GREEK MOUSSAKA MEATLESS MEATBALLS, GREEK MOUSSAKA",
                    "628025017508"),
            new Food(2643278,
                    "MOUSSAKA WITH CREAMY BECHAMEL SAUCE",
                    "860004129806")
    ));

    private static final String SERIALIZED_FOODS =
            "700884@GREEK MOUSSAKA MEATLESS MEATBALLS, GREEK MOUSSAKA@628025017508" +
                    "#" +
                    "2643278@MOUSSAKA WITH CREAMY BECHAMEL SAUCE@860004129806";

    @Test
    void testSerializeIsCorrect() {
        String actual = FOODS.serialize();
        System.out.println(actual);

        assertEquals(SERIALIZED_FOODS, actual,
                "Expected: " + SERIALIZED_FOODS + " but it was: " + actual);
    }

    @Test
    void testDeserializeIsCorrect() {
        Foods actual = Foods.deserialize(SERIALIZED_FOODS);

        assertEquals(FOODS, actual,
                "Expected: " + FOODS + " but it was: " + actual);
    }
}
