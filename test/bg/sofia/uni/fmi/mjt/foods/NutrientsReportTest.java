package bg.sofia.uni.fmi.mjt.foods;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NutrientsReportTest {
    private static final Nutrient FAT = new Nutrient(0.0);
    private static final Nutrient FIBER = new Nutrient(1.03);
    private static final Nutrient SUGARS = new Nutrient(3.0);
    private static final Nutrient PROTEIN = new Nutrient(1.0);
    private static final Nutrient CALORIES = new Nutrient(25.1);
    private static final NutrientsReport NUTRIENT_REPORT_WITH_ALL_VARIABLES =
            new NutrientsReport(FAT, FIBER, SUGARS, PROTEIN, CALORIES);
    private static final NutrientsReport NUTRIENT_REPORT_WITH_NULL_VARIABLES =
            new NutrientsReport(FAT, null, SUGARS, null, CALORIES);
    private static final String SERIALIZED_NUTRIENT_REPORT_ALL_VALUES = "0.0#1.03#3.0#1.0#25.1";
    private static final String SERIALIZED_NUTRIENT_REPORT_NULL_VALUES = "0.0#null#3.0#null#25.1";

    @Test
    void testSerializeIsCorrect() {
        String actualWithAllValues = NUTRIENT_REPORT_WITH_ALL_VARIABLES.serialize();
        String actualWithNullValues = NUTRIENT_REPORT_WITH_NULL_VARIABLES.serialize();

        assertEquals(SERIALIZED_NUTRIENT_REPORT_ALL_VALUES, actualWithAllValues,
                "Expected: " + SERIALIZED_NUTRIENT_REPORT_ALL_VALUES +
                        " but it was: " + actualWithAllValues);

        assertEquals(SERIALIZED_NUTRIENT_REPORT_NULL_VALUES, actualWithNullValues,
                "Expected: " + SERIALIZED_NUTRIENT_REPORT_NULL_VALUES +
                        " but it was: " + actualWithNullValues);
    }

    @Test
    void testDeserializeIsCorrect() {
        NutrientsReport actualWithAllValues =
                NutrientsReport.deserialize(SERIALIZED_NUTRIENT_REPORT_ALL_VALUES);
        NutrientsReport actualWithNullValues =
                NutrientsReport.deserialize(SERIALIZED_NUTRIENT_REPORT_NULL_VALUES);

        assertEquals(NUTRIENT_REPORT_WITH_ALL_VARIABLES, actualWithAllValues,
                "Expected: " + NUTRIENT_REPORT_WITH_ALL_VARIABLES +
                        " but it was: " + actualWithAllValues);

        assertEquals(NUTRIENT_REPORT_WITH_NULL_VARIABLES, actualWithNullValues,
                "Expected: " + NUTRIENT_REPORT_WITH_NULL_VARIABLES +
                        " but it was: " + actualWithNullValues);
    }
}
