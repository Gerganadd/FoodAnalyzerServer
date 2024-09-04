package bg.sofia.uni.fmi.mjt.foods;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NutrientTest {
    private static final Nutrient N1 = new Nutrient(1.2345);
    private static final Nutrient N2 = new Nutrient(0);
    private static final Nutrient N3 = new Nutrient(0.002);
    private static final String SERIALIZED_N1 = "1.2345";
    private static final String SERIALIZED_N2 = "0.0";
    private static final String SERIALIZED_N3 = "0.002";

    @Test
    void testSerializeIsCorrect() {
        String actualN1 = N1.serialize();
        String actualN2 = N2.serialize();
        String actualN3 = N3.serialize();

        assertEquals(SERIALIZED_N1, actualN1,
                "Expected: " + SERIALIZED_N1 + " but it was: " + actualN1);
        assertEquals(SERIALIZED_N2, actualN2,
                "Expected: " + SERIALIZED_N2 + " but it was: " + actualN2);
        assertEquals(SERIALIZED_N3, actualN3,
                "Expected: " + SERIALIZED_N3 + " but it was: " + actualN3);
    }

    @Test
    void testDeserializeIsCorrect() {
        Nutrient actualN1 = Nutrient.deserialize(SERIALIZED_N1);
        Nutrient actualN2 = Nutrient.deserialize(SERIALIZED_N2);
        Nutrient actualN3 = Nutrient.deserialize(SERIALIZED_N3);

        assertEquals(N1, actualN1,
                "Expected: " + N1 + " but it was: " + actualN1);
        assertEquals(N2, actualN2,
                "Expected: " + N2 + " but it was: " + actualN2);
        assertEquals(N3, actualN3,
                "Expected: " + N3 + " but it was: " + actualN3);
    }
}
