package bg.sofia.uni.fmi.mjt.http;

import bg.sofia.uni.fmi.mjt.exceptions.ExceptionMessages;
import bg.sofia.uni.fmi.mjt.exceptions.NoSuchElementException;
import bg.sofia.uni.fmi.mjt.foods.Food;
import bg.sofia.uni.fmi.mjt.foods.FoodReport;
import bg.sofia.uni.fmi.mjt.foods.Foods;

import com.google.gson.Gson;

import java.io.IOException;

import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class HttpRespondFactory {
    private static final Gson gson = new Gson();

    private HttpRespondFactory() {
        // don't want instances of this class
    }

    public static Foods getFoodsByNameFromApi(String name) throws NoSuchElementException {
        //to-do validate code

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
        //to-do validate code

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
        //to-do validate fdcId

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
            throw new IllegalArgumentException("Make custom exception"); // to-do make custom exception
        }
    }

}
