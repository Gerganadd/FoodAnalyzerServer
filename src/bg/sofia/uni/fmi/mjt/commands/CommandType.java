package bg.sofia.uni.fmi.mjt.commands;

public enum CommandType {
    GET_FOOD("get-food"),
    GET_FOOD_REPORT("get-food-report"),
    GET_FOOD_BY_BARCODE("get-food-by-barcode");

    private final String text;

    CommandType(String text) {
        this.text = text;
    }

    public static CommandType getValueOf(String text) {
        if (GET_FOOD.getText().equals(text)) {
            return GET_FOOD;
        }
        if (GET_FOOD_REPORT.getText().equals(text)) {
            return GET_FOOD_REPORT;
        }
        if (GET_FOOD_BY_BARCODE.getText().equals(text)) {
            return GET_FOOD_BY_BARCODE;
        }

        throw new IllegalArgumentException("Unknown command type: " + text);
    }

    public String getText() {
        return text;
    }

}
