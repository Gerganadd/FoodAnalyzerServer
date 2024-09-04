package bg.sofia.uni.fmi.mjt.http;

import bg.sofia.uni.fmi.mjt.foods.Food;
import org.junit.jupiter.api.Test;

import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HttpRequestFactoryTest {
    private static final Food TOMATOES = new Food(
            2543214, "TOMATOES", "04656702273");
    private static final Food RAFFAELLO_TREAT =
            new Food(2041155, "RAFFAELLO TREAT", "009800146130");

    @Test
    void testCreateRequestForFoodByGtinUpcCodeWithNullCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByGtinUpcCode(null),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodByGtinUpcCodeWithEmptyCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByGtinUpcCode(""),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByGtinUpcCode(" "),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodByGtinUpcCodeWithInvalidCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByGtinUpcCode("127w67"),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByGtinUpcCode("1@34"),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByGtinUpcCode("123 784"),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodByGtinUpcCodeWithValidCode() {
        String gtinUpc = TOMATOES.gtinUpc();

        assertDoesNotThrow(() -> HttpRequestFactory.createRequestForFoodByGtinUpcCode(gtinUpc),
                "Expected not to throw exception");

        HttpRequest request = HttpRequestFactory.createRequestForFoodByGtinUpcCode(gtinUpc);

        assertNotNull(request, "Don't expect request to be null");
        assertFalse(request.uri().toString().isBlank(), "Don't expect request uri to be blank");
    }

    @Test
    void testCreateRequestForFoodByFcdIdWithNullCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByFcdId(null),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodByFcdIdWithEmptyCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByFcdId(""),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByFcdId(" "),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodByFcdIdWithInvalidCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByFcdId("127w67"),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByFcdId("1@34"),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodByFcdId("123 784"),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodByFcdIdWithValidCode() {
        String fcdId = String.valueOf(TOMATOES.fdcId());

        assertDoesNotThrow(() -> HttpRequestFactory.createRequestForFoodByFcdId(fcdId),
                "Expected not to throw exception");

        HttpRequest request = HttpRequestFactory.createRequestForFoodByFcdId(fcdId);

        assertNotNull(request, "Don't expect request to be null");
        assertFalse(request.uri().toString().isBlank(), "Don't expect request uri to be blank");
    }

    @Test
    void testCreateRequestForFoodsByNameWithNullCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodsByName(null),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodsByNameWithEmptyCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodsByName(""),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodsByName(" "),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodsByNameWithInvalidCode() {
        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodsByName("tomatoes@"),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodsByName("peperoni & cheese"),
                "Expected IllegalArgumentException but wasn't thrown");

        assertThrows(IllegalArgumentException.class,
                () -> HttpRequestFactory.createRequestForFoodsByName("@potatoes onion cheddar%"),
                "Expected IllegalArgumentException but wasn't thrown");
    }

    @Test
    void testCreateRequestForFoodsByNameWithValidName() {
        String name = TOMATOES.description();

        assertDoesNotThrow(() -> HttpRequestFactory.createRequestForFoodsByName(name),
                "Expected not to throw exception");

        HttpRequest request = HttpRequestFactory.createRequestForFoodsByName(name);

        assertNotNull(request, "Don't expect request to be null");
        assertFalse(request.uri().toString().isBlank(), "Don't expect request uri to be blank");
    }

    @Test
    void testCreateRequestForFoodsByNameWithValidNameMultipleNames() {
        String name = RAFFAELLO_TREAT.description();

        assertDoesNotThrow(() -> HttpRequestFactory.createRequestForFoodsByName(name),
                "Expected not to throw exception");

        HttpRequest request = HttpRequestFactory.createRequestForFoodsByName(name);

        assertNotNull(request, "Don't expect request to be null");
        assertFalse(request.uri().toString().isBlank(), "Don't expect request uri to be blank");
    }
}
