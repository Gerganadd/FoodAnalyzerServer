package bg.sofia.uni.fmi.mjt.foods;

import bg.sofia.uni.fmi.mjt.regexs.Regex;

import java.io.Serializable;

public record Nutrient(double value) implements Serializable {
    private static final String FORMAT_DOUBLE_TWO_DIGITS = "%.2f";

    public static Nutrient deserialize(String info) {
        info = info.trim().replaceAll(Regex.MATCH_COMMA, Regex.MATCH_DOT);
        double value = Double.parseDouble(info);

        return new Nutrient(value);
    }

    public String serialize() {
        return String.format(FORMAT_DOUBLE_TWO_DIGITS, value);
    }
}
