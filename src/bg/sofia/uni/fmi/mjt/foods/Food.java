package bg.sofia.uni.fmi.mjt.foods;

import java.io.Serializable;
import java.util.List;

public record Food(long fdcId, String description, String gtinUpc) implements Serializable {
    private static final String EMPTY_VALUE = "null";
    private static final String SEPARATOR = "@";
    private static final int FDC_ID_INDEX = 0;
    private static final int DESCRIPTION_INDEX = 1;
    private static final int GTIN_UPC_INDEX = 2;

    public static Food deserialize(String info) {
        String[] args = info.split(SEPARATOR);

        long fdcId = deserializeFcdId(args[FDC_ID_INDEX]);
        String description = deserializeDescription(args[DESCRIPTION_INDEX]);
        String gtinUpc = deserializeGtinUpcCode(args[GTIN_UPC_INDEX]);

        return new Food(fdcId, description, gtinUpc);
    }

    private static long deserializeFcdId(String code) {
        return Long.parseLong(code.trim());
    }

    private static String deserializeDescription(String description) {
        return description.trim();
    }

    private static String deserializeGtinUpcCode(String code) {
        code = code.trim();

        if (code.equals(EMPTY_VALUE)) {
            return null;
        }

        return code;
    }

    private static String serializeGtinUpcCode(String code) {
        return code == null ? EMPTY_VALUE : code;
    }

    public String serialize() {
        List<String> args = List.of(Long.toString(fdcId), description, serializeGtinUpcCode(gtinUpc));

        return String.join(SEPARATOR, args);
    }
}
