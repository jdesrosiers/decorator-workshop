package books;

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

public class BookServiceSqliteTest {
    private Connection connection;

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
    }

    @After
    public void tearDown() throws SQLException {
        Statement statement = connection.createStatement();
        statement.executeUpdate("DELETE FROM books");

        connection.close();
    }

    @Test
    public void getBookShouldRetrieveABook() throws ClassNotFoundException, SQLException, BookstoreException {
        loadFixtrue1();
        BookService service = new BookServiceSqlite(connection);

        Book book = service.get(1);

        assertThat(book, equalTo(bookFixture1));
    }

    @Test
    public void getAllBooksShouldRetrieveAListOfBooks() throws ClassNotFoundException, SQLException, BookstoreException {
        loadFixtrue1();
        loadFixtrue2();
        BookService service = new BookServiceSqlite(connection);

        List<Integer> book = service.getAll();

        assertThat(book.size(), equalTo(2));
        assertThat(book.get(0), equalTo(1));
        assertThat(book.get(1), equalTo(2));
    }

    @Test
    public void deleteBookShouldRemoveABook() throws SQLException, ClassNotFoundException, BookstoreException {
        loadFixtrue1();
        BookService service = new BookServiceSqlite(connection);

        service.delete(1);

        thrown.expect(BookstoreException.class);
        thrown.expectMessage("Book Not Found");
        service.get(1);
    }

    @Test
    public void saveBookShouldUpdateTheBookToMatchTheGivenBook() throws SQLException, ClassNotFoundException, BookstoreException {
        loadFixtrue1();
        BookService service = new BookServiceSqlite(connection);

        Book newBook = new Book(1, "Foo", "Bar", Arrays.asList("Me"), Year.of(2017));
        service.save(newBook);

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