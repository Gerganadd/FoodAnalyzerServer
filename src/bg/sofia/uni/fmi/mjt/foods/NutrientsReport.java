package bg.sofia.uni.fmi.mjt.foods;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import java.util.stream.Stream;

public record NutrientsReport(Nutrient fat, Nutrient fiber, Nutrient sugars,
                              Nutrient protein, Nutrient calories) implements Serializable {
    private static final String SEPARATOR = "#";
    private static final int FAT_INDEX = 0;
    private static final int FIBER_INDEX = 1;
    private static final int SUGARS_INDEX = 2;
    private static final int PROTEIN_INDEX = 3;
    private static final int CALORIES_INDEX = 4;

    public static NutrientsReport deserialize(String info) {
        String[] args = info.split(SEPARATOR);

        Nutrient fat = Nutrient.deserialize(args[FAT_INDEX]);
        Nutrient fiber = Nutrient.deserialize(args[FIBER_INDEX]);
        Nutrient sugars = Nutrient.deserialize(args[SUGARS_INDEX]);
        Nutrient protein = Nutrient.deserialize(args[PROTEIN_INDEX]);
        Nutrient calories = Nutrient.deserialize(args[CALORIES_INDEX]);

        return new NutrientsReport(fat, fiber, sugars, protein, calories);
    }

    public String serialize() {
        List<Nutrient> nutrients = Stream.of(fat, fiber, sugars, protein, calories).toList();
        List<String> serializedNutrients = new ArrayList<>();

        for (Nutrient nutrient : nutrients) {
            if (nutrient == null) {
                serializedNutrients.add(Nutrient.EMPTY_VALUE);
            } else {
                serializedNutrients.add(nutrient.serialize());
            }
        }

        return String.join(SEPARATOR, serializedNutrients);
    }
}
