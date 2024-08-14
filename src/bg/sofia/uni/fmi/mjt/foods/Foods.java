package bg.sofia.uni.fmi.mjt.foods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public record Foods(List<Food> foods) implements Serializable {
    private static final String SEPARATOR = "#";

    public static Foods deserialize(String info) {
        String[] args = info.split(SEPARATOR);
        List<Food> foodsList = new ArrayList<>();

        for (String arg : args) {
            foodsList.add(Food.deserialize(arg.trim()));
        }

        return new Foods(foodsList);
    }

    public String serialize() {
        List<String> args = foods.stream().map(Food::serialize).toList();
        return String.join(SEPARATOR, args);
    }
}
