package books;

import org.apache.commons.jcs.access.CacheAccess;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.*;

public class BookServiceTest {
    private Connection connection;
    private CacheAccess<Integer, Book> cache;

    private Book bookFixture1 = new Book(
        1,
        "Agile Software Development",
        "Principles, Patterns, and Practices",
        Arrays.asList("Robert C. Martin"),
        Year.of(2003)
    );
    private Book bookFixture2 = new Book(
        2,
        "Design Patterns",
        "Elements of Reusable Object-Oriented Software",
        Arrays.asList("Erich Gamma", "Richard Helm", "Ralph Johnson", "John Vlissides"),
        Year.of(1995)
    );

    @Rule
    public ExpectedException thrown = ExpectedException.none();

    @Before
    @SuppressWarnings("unchecked")
    public void setUp() throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");
        connection = DriverManager.getConnection("jdbc:sqlite:bookstore_test.db");
        cache = mock(CacheAccess.class);
    }

    @After
    public void tearDown() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM books");

        connection.close();
    }

    @Test
    public void getBookShouldRetrieveABookFromTheDatabaseOnACacheMiss() throws ClassNotFoundException, SQLException, BookstoreException {
        loadFixtrue1();
        BookService service = new BookService(connection, cache);

        when(cache.get(1)).thenReturn(null);
        Book book = service.get(1);

        assertThat(book, equalTo(bookFixture1));
    }

    @Test
    public void getBookShouldNotCallTheDatabaseForABookThatHasBeenCached() throws ClassNotFoundException, SQLException, BookstoreException {
        Connection connection = mock(Connection.class);
        BookService service = new BookService(connection, cache);

        when(cache.get(1)).thenReturn(bookFixture1);
        Book book = service.get(1);

        verify(connection, never()).prepareStatement(any());
        assertThat(book, equalTo(bookFixture1));
    }

    @Test
    public void getAllBooksShouldRetrieveBooksFromTheDatabaseOnACacheMiss() throws ClassNotFoundException, SQLException, BookstoreException {
        loadFixtrue1();
        loadFixtrue2();
        BookService service = new BookService(connection, cache);

        when(cache.get(1)).thenReturn(null);
        when(cache.get(2)).thenReturn(null);
        List<Integer> book = service.getAll();

        assertThat(book.size(), equalTo(2));
        assertThat(book.get(0), equalTo(1));
        assertThat(book.get(1), equalTo(2));
    }

    @Test
    public void getAllBooksShouldNotCallTheDatabaseForBooksThatAreCached() throws ClassNotFoundException, SQLException, BookstoreException {
        loadFixtrue1();
        loadFixtrue2();
        BookService service = new BookService(connection, cache);

        when(cache.get(1)).thenReturn(bookFixture1);
        when(cache.get(2)).thenReturn(bookFixture2);
        List<Integer> book = service.getAll();

        assertThat(book.size(), equalTo(2));
        assertThat(book.get(0), equalTo(1));
        assertThat(book.get(1), equalTo(2));
    }

    @Test
    public void deleteBookShouldRemoveABookFromTheDatabaseAndTheCache() throws SQLException, ClassNotFoundException, BookstoreException {
        loadFixtrue1();
        BookService service = new BookService(connection, cache);

        service.delete(1);
        verify(cache).remove(1);

        thrown.expect(BookstoreException.class);
        thrown.expectMessage("Book Not Found");
        service.get(1);
    }

    @Test
    public void saveBookShouldUpdateTheBookInTheDatabaseToMatchTheGivenBookAndUpdateTheCache() throws SQLException, ClassNotFoundException, BookstoreException {
        loadFixtrue1();
        BookService service = new BookService(connection, cache);

        Book newBook = new Book(1, "Foo", "Bar", Arrays.asList("Me"), Year.of(2017));
        service.save(newBook);
        verify(cache).put(1, newBook);

        Book savedBook = service.get(1);
        assertThat(savedBook, equalTo(newBook));
    }

    private void loadFixtrue1() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO books VALUES(1, 'Agile Software Development', 'Principles, Patterns, and Practices', 'Robert C. Martin', '2003')");
    }

    private void loadFixtrue2() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("INSERT INTO books VALUES(2, 'Design Patterns', 'Elements of Reusable Object-Oriented Software', 'Erich Gamma,Richard Helm,Ralph Johnson,John Vlissides', '1995')");
    }
}