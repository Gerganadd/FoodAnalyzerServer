package bg.sofia.uni.fmi.mjt.http;

import bg.sofia.uni.fmi.mjt.exceptions.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.regexs.Regex;

import java.net.URI;
import java.net.http.HttpRequest;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRequestFactory {
    private static final String SITE_URL = "https://api.nal.usda.gov/fdc/";
    private static final String API_KEY = String.format("api_key=%s", System.getenv("FOODS_API_KEY"));

    private static final String SEARCH_BY_VALUE = "%s%s?query=%s%s&%s";
    private static final String SEARCH_BY_ID = "%sv1/food/%s?%s";
    private static final String SEARCH_PRODUCT_BY = "v1/foods/search";

    private static final String MATCH_ALL_WORDS = "&requireAllWords=true";
    private static final String HTTP_VERSION_OF_SPACE = "%20";

    public static HttpRequest createRequestForFoodByGtinUpcCode(String gtinUpcCode) {
        validateCode(gtinUpcCode, "Gtin upc");

        String query = createQueryForSearchByValue(gtinUpcCode);

        return createHttpRequest(query);
    }

    public static HttpRequest createRequestForFoodByFcdId(String fdcId) {
        validateCode(fdcId, "fdc id");

        String query = createQueryForSearchById(fdcId);

        return createHttpRequest(query);
    }

    public static HttpRequest createRequestForFoodsByName(String productName) {
        validateProductName(productName);

        productName = formatFoodName(productName);

        String query = createQueryForSearchByValue(productName);

        return createHttpRequest(query);
    }

    private static HttpRequest createHttpRequest(String query) {
        return HttpRequest
                .newBuilder()
                .uri(URI.create(query))
                .build();
    }

    private static String createQueryForSearchByValue(String value) {
        return String.format(SEARCH_BY_VALUE,
                SITE_URL, SEARCH_PRODUCT_BY, value, MATCH_ALL_WORDS, API_KEY);
    }

    private static String createQueryForSearchById(String fdcId) {
        return String.format(SEARCH_BY_ID,
                SITE_URL, fdcId, API_KEY);
    }

    private static String formatFoodName(String productName) {
        return productName.replaceAll(Regex.MATCH_SPACE, HTTP_VERSION_OF_SPACE);
    }

    private static void validateProductName(String productName) {
        if (productName == null || productName.isBlank()) {
            throw new IllegalArgumentException(ExceptionMessages.PRODUCT_NAME_NULL_OR_BLANK);
        }

        productName = productName.trim();

        Pattern pattern = Pattern.compile(
                Regex.START_WITH + Regex.MATCH_ONLY_WORDS + Regex.END_WITH);
        Matcher matcher = pattern.matcher(productName);

        if (!matcher.find()) {
            throw new IllegalArgumentException(ExceptionMessages.PRODUCT_NAME_REQUIREMENTS);
        }
    }

    private static void validateCode(String code, String codeName) {
        if (code == null || code.isBlank()) {
            throw new IllegalArgumentException(codeName + ExceptionMessages.CODE_NULL_OR_BLANK);
        }

        code = code.trim();

        Pattern pattern = Pattern.compile(
                Regex.START_WITH + Regex.MATCH_ONLY_DIGITS + Regex.END_WITH);
        Matcher matcher = pattern.matcher(code);

        if (!matcher.find()) {
            throw new IllegalArgumentException(codeName + ExceptionMessages.CODE_REQUIREMENTS);
        }
    }
}
