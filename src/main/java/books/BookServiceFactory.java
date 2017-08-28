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
            BookService profiledDataService = new BookServiceProfiler(dataService, dataLogger);

            CacheAccess<Integer, Book> cache = JCS.getInstance("bookCache");
            BookService cachedProfiledDataService = new BookServiceCache(profiledDataService, cache);

            Logger cacheLogger = Logger.getLogger("BookServiceCache");
            BookService profiledCachedProfiledDataService = new BookServiceProfiler(cachedProfiledDataService, cacheLogger);

            return profiledCachedProfiledDataService;
        } catch (ClassNotFoundException e) {
            throw new BookstoreException(e);
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }
}
