package bg.sofia.uni.fmi.mjt.foods;

import java.io.Serializable;
import java.util.List;

public record Food(long fdcId, String description, String gtinUpc) implements Serializable {
    private static final String SEPARATOR = "@";
    private static final int FDC_ID_INDEX = 0;
    private static final int DESCRIPTION_INDEX = 1;
    private static final int GTIN_UPC_INDEX = 1;
    
    public static Food deserialize(String info) {
        String[] args = info.split(SEPARATOR);

        long fdcId = Long.parseLong(args[FDC_ID_INDEX]);
        String description = args[DESCRIPTION_INDEX].trim();
        String gtinUpc = args[GTIN_UPC_INDEX].trim();

        return new Food(fdcId, description, gtinUpc);
    }

    public String serialize() {
        List<String> args = List.of(Long.toString(fdcId), description, gtinUpc);
        return String.join(SEPARATOR, args);
    }
}
