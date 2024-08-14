package bg.sofia.uni.fmi.mjt.http;

import org.junit.jupiter.api.Test;

import java.net.URI;
import java.net.http.HttpRequest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class HttpRequestFactoryTest {
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
    void testCreateRequestForFoodByGtinUpcCodeWithValidCode() { // to-do ?
        String query = "";
        HttpRequest expected = HttpRequest.newBuilder().uri(URI.create(query)).build();
        HttpRequest actual = HttpRequestFactory.createRequestForFoodByGtinUpcCode("");

        assertEquals(expected, actual,
                "Expected: "+ expected.toString() + " but it was: " + actual.toString());

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
        //to-do
        throw new RuntimeException("Not ready");
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
    void testCreateRequestForFoodsByNameWithValidCode() {
        // to-do
        throw new RuntimeException("Not ready");
    }
}
