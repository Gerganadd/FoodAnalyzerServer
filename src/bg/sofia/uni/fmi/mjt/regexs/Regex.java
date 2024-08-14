package bg.sofia.uni.fmi.mjt.regexs;

public class Regex {
    public static String MATCH_ONLY_DIGITS = "[0-9]+";
    public static String MATCH_ONLY_WORDS = "[a-zA-z ]+";
    public static String MATCH_IMAGE_ATTRIBUTE = " ?--img=[\\w:\\\\.]+";
    public static String MATCH_BARCODE_ATTRIBUTE = " ?--code=[0-9]+";
    public static String MATCH_IMAGE_AND_BARCODE_ATTRIBUTES =
            String.format("((%s)|(%s)){1,2}", MATCH_IMAGE_ATTRIBUTE, MATCH_BARCODE_ATTRIBUTE);
    public static String START_WITH = "^";
    public static String END_WITH = "$";
    public static String MATCH_SPACE = " ";
    public static String MATCH_EQUALS_SIGN = "=";
}
