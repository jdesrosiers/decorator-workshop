package books;

import java.sql.*;
import java.time.Year;
import java.util.*;

public class BookServiceSqlite implements BookService {
    private Connection connection;

    public BookServiceSqlite(Connection connection) throws ClassNotFoundException, SQLException {
        this.connection = connection;
    }

    @Override
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

    @Override
    public Book get(int id) throws BookstoreException {
        try {
            String query = "SELECT * FROM books WHERE books.id = ? ";
            PreparedStatement bookStmt = connection.prepareStatement(query);
            bookStmt.setInt(1, id);

            ResultSet rs = bookStmt.executeQuery();
            if (rs.next()) {
                return new Book(
                    rs.getInt("id"),
                    rs.getString("title"),
                    rs.getString("subTitle"),
                    Arrays.asList(rs.getString("authors").split(",")),
                    Year.of(rs.getInt("publishDate"))
                );
            } else {
                throw new BookstoreException("Book Not Found");
            }
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }

    @Override
    public void delete(int id) throws BookstoreException {
        try {
            PreparedStatement deleteStmt = connection.prepareStatement("DELETE FROM books WHERE id = ?");
            deleteStmt.setInt(1, id);
            deleteStmt.executeUpdate();
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }

    @Override
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
        } catch (SQLException e) {
            throw new BookstoreException(e);
        }
    }
}
