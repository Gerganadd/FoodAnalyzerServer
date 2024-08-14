package bg.sofia.uni.fmi.mjt.foods;

import java.io.Serializable;

public record Nutrient(double value) implements Serializable {
    private static final String FORMAT_DOUBLE_TWO_DIGITS = "%.2f";

    public static Nutrient deserialize(String info) {
        double value = Double.parseDouble(info.trim());

        return new Nutrient(value);
    }

    public String serialize() {
        return String.format(FORMAT_DOUBLE_TWO_DIGITS, value);
    }
}
