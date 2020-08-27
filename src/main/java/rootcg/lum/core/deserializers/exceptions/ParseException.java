package rootcg.lum.core.deserializers.exceptions;

public class ParseException extends Exception {

    private String block;

    public ParseException(String block) {
        super("Illegal definition: " + block);
    }

    public String getBlock() {
        return block;
    }

}
