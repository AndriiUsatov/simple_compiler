package andrii.app.univ.exception;

public class ParseException extends RuntimeException{

    public ParseException(String text, Exception e) {
        super(text, e);
    }
}
