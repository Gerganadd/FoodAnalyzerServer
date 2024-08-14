package bg.sofia.uni.fmi.mjt.http;

import bg.sofia.uni.fmi.mjt.foods.Food;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class HttpRespondFactoryTest {

    @Test
    void testGetFoodByBarcodeFromApiWithValidName() {
        Food expected = new Food(2453490, "CUCUMBER HUMMUS, CUCUMBER", "020601412378");
        Food actual = HttpRespondFactory.getFoodByBarcodeFromApi("020601412378");

        Assertions.assertEquals(expected, actual,
                "Expected: " + expected + " but it was: " + actual);
    }
}
