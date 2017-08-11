package books;

import org.apache.commons.jcs.access.CacheAccess;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.Year;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class BookService {
    private Connection connection;
    private CacheAccess<Integer, Book> cache;

    public BookService(Connection connection, CacheAccess<Integer, Book> cache) throws ClassNotFoundException, SQLException {
        this.connection = connection;
        this.cache = cache;
    }

    public List<Integer> getAll() throws BookstoreException {
        try {
            Statement statement = connection.createStatement();

            ResultSet rs = statement.executeQuery("SELECT id FROM books");
            List<Integer> result = new ArrayList<>();
            while (rs.next()) {
                result.add(rs.getInt("id"));
            }

            return result;
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }

    public Book get(int id) throws BookstoreException {
        Book book = cache.get(id);

        if (book == null) {
            try {
                String query = "SELECT * FROM books WHERE books.id = ? ";
                PreparedStatement bookStmt = connection.prepareStatement(query);
                bookStmt.setInt(1, id);

                ResultSet rs = bookStmt.executeQuery();
                if (rs.next()) {
                    book = new Book(
                        rs.getInt("id"),
                        rs.getString("title"),
                        rs.getString("subTitle"),
                        Arrays.asList(rs.getString("authors").split(",")),
                        Year.of(rs.getInt("publishDate"))
                    );

                    cache.put(book.getId(), book);
                } else {
                    throw new BookstoreException("Book Not Found");
                }
            } catch (SQLException e) {
                throw new BookstoreException(e);
            }
        }

        return book;
    }

    public void delete(int id) throws BookstoreException {
        try {
            PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM books WHERE id = ?");
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();

            cache.remove(id);
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }

    public void save(Book book) throws BookstoreException {
        try {
            String update =
                "UPDATE books " +
                "SET title = ?, subTitle = ?, authors = ?, publishDate = ?" +
                "WHERE id = ?";
            PreparedStatement updateStmt = connection.prepareStatement(update);
            updateStmt.setString(1, book.getTitle());
            updateStmt.setString(2, book.getSubtitle());
            updateStmt.setString(3, String.join(",", book.getAuthors()));
            updateStmt.setObject(4, book.getPublishDate());
            updateStmt.setInt(5, book.getId());

            updateStmt.executeUpdate();

            cache.put(1, book);
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }
}
