package net.irregularexpressions.nintex.shortener;

public class CodeInUseException extends Exception {
    public CodeInUseException() {
    }

    public CodeInUseException(String message) {
        super(message);
    }

    public CodeInUseException(String message, Throwable cause) {
        super(message, cause);
    }

    public CodeInUseException(Throwable cause) {
        super(cause);
    }

    public CodeInUseException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
