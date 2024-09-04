package bg.sofia.uni.fmi.mjt.foods;

import bg.sofia.uni.fmi.mjt.regexs.Regex;

import java.io.Serializable;

public record Nutrient(double value) implements Serializable {
    public static final String EMPTY_VALUE = "null";

    public static Nutrient deserialize(String info) {
        info = info.trim().replaceAll(Regex.MATCH_COMMA, Regex.MATCH_DOT);

        if (info.equals(EMPTY_VALUE)) {
            return null;
        }

        double value = Double.parseDouble(info);

        return new Nutrient(value);
    }

    public String serialize() {
        return String.valueOf(value);
    }
}
