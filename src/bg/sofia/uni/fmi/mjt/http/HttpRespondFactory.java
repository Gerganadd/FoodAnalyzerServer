package bg.sofia.uni.fmi.mjt.http;

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

    public static Foods getFoodsByNameFromApi(String name) {
        //to-do validate name

        HttpRequest requestForFoodByKeyword = HttpRequestFactory.createRequestForFoodsByName(name);
        HttpResponse<String> responseFromApi = getFromApi(requestForFoodByKeyword);

        Foods respond = gson.fromJson(responseFromApi.body(), Foods.class);

        return respond;
    }

    public static Food getFoodByBarcodeFromApi(String code) {
        //to-do validate code

        HttpRequest requestForFoodByKeyword = HttpRequestFactory.createRequestForFoodByGtinUpcCode(code);
        HttpResponse<String> responseFromApi = getFromApi(requestForFoodByKeyword);

        Foods respond = gson.fromJson(responseFromApi.body(), Foods.class);

        return respond.foods().getFirst();
    }

    public static FoodReport getFoodReportByFdcIdFromApi(String fdcId) {
        //to-do validate fdcId

        HttpRequest requestByFcdId = HttpRequestFactory.createRequestForFoodByFcdId(fdcId);
        HttpResponse<String> responseFromApi = getFromApi(requestByFcdId);

        return gson.fromJson(responseFromApi.body(), FoodReport.class);
    }

    private static HttpResponse<String> getFromApi(HttpRequest request) {
        try (HttpClient client = HttpClient.newHttpClient()) {

            return client.send(request, HttpResponse.BodyHandlers.ofString());

        } catch (IOException | InterruptedException e) {
            throw new IllegalArgumentException("Make custom exception"); // to-do make custom exception
        }
    }

}
