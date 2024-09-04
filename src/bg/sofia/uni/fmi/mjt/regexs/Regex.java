package bg.sofia.uni.fmi.mjt.regexs;

public class Regex {
    public static final String MATCH_ONLY_DIGITS = "[0-9]+";
    public static final String MATCH_ONLY_WORDS = "[a-zA-z ]+";
    public static final String MATCH_IMAGE_ATTRIBUTE = " ?--img=[\\w:\\\\.]+";
    public static final String MATCH_BARCODE_ATTRIBUTE = " ?--code=[0-9]+";
    public static final String MATCH_IMAGE_AND_BARCODE_ATTRIBUTES =
            String.format("((%s)|(%s)){1,2}", MATCH_IMAGE_ATTRIBUTE, MATCH_BARCODE_ATTRIBUTE);
    public static final String START_WITH = "^";
    public static final String END_WITH = "$";
    public static final String MATCH_SPACE = " ";
    public static final String MATCH_COMMA = ",";
    public static final String MATCH_DOT = ".";
    public static final String MATCH_EQUALS_SIGN = "=";
}
