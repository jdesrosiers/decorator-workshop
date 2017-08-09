package books;

public class BookstoreException extends Throwable {
    public BookstoreException() {
       super();
    }

    public BookstoreException(String message) {
       super(message);
    }

    public BookstoreException(String message, Throwable cause) {
       super(message, cause);
    }

    protected BookstoreException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
       super(message, cause, enableSuppression, writableStackTrace);
    }

    public BookstoreException(Throwable cause) {
        super(cause);
    }
}
