package bg.sofia.uni.fmi.mjt.foods;

public record FoodReport(long fdcId, String description,
                         String ingredients, NutrientsReport labelNutrients) {
}
