package bg.sofia.uni.fmi.mjt.http;

import java.net.URI;
import java.net.http.HttpRequest;

public class HttpRequestFactory {
    private static final String SITE_URL = "https://api.nal.usda.gov/fdc/";
    private static final String API_KEY_VALUE = "2vPhFALHihIOLVYt0VjFvRJJjpFRsnFGR38NoM8K";
    private static final String API_KEY = String.format("api_key=%s", API_KEY_VALUE);
    private static final String SEARCH_PRODUCT_BY = "v1/foods/search";
    private static final String MATCH_ALL_WORDS = "&requireAllWords=true";

    private static final String REGEX_MATCH_SPACE = " ";
    private static final String HTTP_VERSION_OF_SPACE = "%20";

    private HttpRequestFactory() {
        // don't want instances of this class
    }

    public static HttpRequest createRequestForFoodByGtinUpcCode(String gtinUpcCode) {
        //to-do validate gtinUpcCode

        String query = createQueryForSearchByValue(gtinUpcCode);

        return createHttpRequest(query);
    }

    public static HttpRequest createRequestForFoodByFcdId(String fdcId) {
        //to-do validate fdcId

        String query = createQueryForSearchById(fdcId);

        return createHttpRequest(query);
    }

    public static HttpRequest createRequestForFoodsByName(String productName) {
        //to-do validate product name
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
        return String.format("%s%s?query=%s%s&%s",
                SITE_URL, SEARCH_PRODUCT_BY, value, MATCH_ALL_WORDS, API_KEY);
    }

    private static String createQueryForSearchById(String fdcId) {
        return String.format("%sv1/food/%s?%s",
                SITE_URL, fdcId, API_KEY);
    }

    private static String formatFoodName(String productName) {
        return productName.replaceAll(REGEX_MATCH_SPACE, HTTP_VERSION_OF_SPACE);
    }

}
