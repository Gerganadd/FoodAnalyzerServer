package bg.sofia.uni.fmi.mjt.http;

import bg.sofia.uni.fmi.mjt.exceptions.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.Food;
import bg.sofia.uni.fmi.mjt.foods.FoodReport;
import bg.sofia.uni.fmi.mjt.foods.Foods;

import bg.sofia.uni.fmi.mjt.regexs.Regex;
import com.google.gson.Gson;

import java.io.IOException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class HttpRespondFactory {
    private static final Gson gson = new Gson();

    public static Foods getFoodsByNameFromApi(String name) throws NoSuchElementException {
        validateProductName(name);

        HttpRequest requestForFoodByKeyword = HttpRequestFactory.createRequestForFoodsByName(name);
        HttpResponse<String> responseFromApi = getFromApi(requestForFoodByKeyword);

        Foods result = gson.fromJson(responseFromApi.body(), Foods.class);

        if (result.foods().isEmpty()) { // Api does not contain information
            throw new NoSuchElementException(
                    ExceptionMessages.FOOD_NAME_DOES_NOT_CONTAINS + name);
        }

        return result;
    }

    public static Food getFoodByBarcodeFromApi(String code) throws NoSuchElementException {
        validateCode(code, "Gtin upc");

        HttpRequest requestForFoodByKeyword = HttpRequestFactory.createRequestForFoodByGtinUpcCode(code);
        HttpResponse<String> responseFromApi = getFromApi(requestForFoodByKeyword);

        Foods result = gson.fromJson(responseFromApi.body(), Foods.class);

        if (result.foods().isEmpty()) { // Api does not contain information
            throw new NoSuchElementException(
                    ExceptionMessages.GTIN_UPC_CODE_DOES_NOT_CONTAINS + code);
        }

        return result.foods().getFirst();
    }

    public static FoodReport getFoodReportByFdcIdFromApi(String fdcId) throws NoSuchElementException {
        validateCode(fdcId, "Fdc id");

        HttpRequest requestByFcdId = HttpRequestFactory.createRequestForFoodByFcdId(fdcId);
        HttpResponse<String> responseFromApi = getFromApi(requestByFcdId);

        FoodReport result = gson.fromJson(responseFromApi.body(), FoodReport.class);

        if (result == null) {
            throw new NoSuchElementException(
                    ExceptionMessages.FCD_ID_DOES_NOT_CONTAINS + fdcId);
        }

        return result;
    }

    private static HttpResponse<String> getFromApi(HttpRequest request) {
        try (HttpClient client = HttpClient.newHttpClient()) {

            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException(ExceptionMessages.PROBLEM_WITH_API, e);
        }
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
