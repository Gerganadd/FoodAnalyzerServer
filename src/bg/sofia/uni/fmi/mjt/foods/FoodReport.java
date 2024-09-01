package bg.sofia.uni.fmi.mjt.foods;

import bg.sofia.uni.fmi.mjt.regexs.Regex;

import java.io.Serializable;
import java.util.List;

public record FoodReport(long fdcId, String description, String ingredients,
                         NutrientsReport labelNutrients) implements Serializable {
    private static final String EMPTY_VALUE = "";
    private static final String SEPARATOR = "@";
    private static final int FDC_ID_INDEX = 0;
    private static final int DESCRIPTION_INDEX = 1;
    private static final int INGREDIENTS_INDEX = 2;
    private static final int NUTRIENTS_INDEX = 3;

    public static FoodReport deserialize(String info) {
        String[] args = info.split(SEPARATOR);

        long fdcId = Long.parseLong(args[FDC_ID_INDEX]);
        String description = args[DESCRIPTION_INDEX].trim();
        String ingredients = args[INGREDIENTS_INDEX].trim();
        NutrientsReport labelNutrients = NutrientsReport.deserialize(args[NUTRIENTS_INDEX]);

        return new FoodReport(fdcId, description, ingredients, labelNutrients);
    }

    public String serialize() {
        String formattedDescription = description.replaceAll(Regex.MATCH_SPECIAL_SYMBOLS, EMPTY_VALUE);
        String formattedIngredients = ingredients.replaceAll(Regex.MATCH_SPECIAL_SYMBOLS, EMPTY_VALUE);

        List<String> args = List.of(Long.toString(fdcId), formattedDescription,
                formattedIngredients, labelNutrients.serialize());
        return String.join(SEPARATOR, args);
    }
}
