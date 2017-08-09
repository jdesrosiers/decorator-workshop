package books;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;

public class BookServiceFactory {
    public BookService getBookService() throws BookstoreException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:bookstore.db");
            CacheAccess<Integer, Book> cache = JCS.getInstance("bookCache");

            return new BookService(connection, cache);
        } catch (ClassNotFoundException e) {
            throw new BookstoreException(e);
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }
}
