package books;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Logger;

import org.apache.commons.jcs.JCS;
import org.apache.commons.jcs.access.CacheAccess;

public class BookServiceFactory {
    public BookService getBookService() throws BookstoreException {
        try {
            Class.forName("org.sqlite.JDBC");
            Connection connection = DriverManager.getConnection("jdbc:sqlite:bookstore.db");
            BookService dataService = new BookServiceSqlite(connection);

            Logger dataLogger = Logger.getLogger("BookServiceSqlite");
            BookService loggedDataService = new BookServiceLogger(dataService, dataLogger);

            CacheAccess<Integer, Book> cache = JCS.getInstance("bookCache");
            BookService cachedLoggedDataService = new BookServiceCache(loggedDataService, cache);

            Logger cacheLogger = Logger.getLogger("BookServiceCache");
            BookService loggedCachedLoggedDataService = new BookServiceLogger(cachedLoggedDataService, cacheLogger);

            return loggedCachedLoggedDataService;
        } catch (ClassNotFoundException e) {
            throw new BookstoreException(e);
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }
}
