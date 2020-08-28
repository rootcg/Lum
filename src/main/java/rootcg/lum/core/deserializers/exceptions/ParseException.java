package rootcg.lum.core.deserializers.exceptions;

public class ParseException extends Exception {

    private static final String template = "Error parsing line %d: %s";

    public ParseException(long line, String message) {
        super(String.format(template, line, message));
    }

    public ParseException(long line, String message, Throwable e) {
        super(String.format(template, line, message), e);
    }

}
