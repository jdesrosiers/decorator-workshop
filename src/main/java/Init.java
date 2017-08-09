import java.sql.*;

public class Init {
    public static void main(String[] args) throws ClassNotFoundException, SQLException {
        Class.forName("org.sqlite.JDBC");

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bookstore.db")) {
            createTables(connection);
            loadData(connection);
        }

        try (Connection connection = DriverManager.getConnection("jdbc:sqlite:bookstore_test.db")) {
            createTables(connection);
        }
    }

    private static void createTables(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("DROP TABLE IF EXISTS books");
        statement.executeUpdate("CREATE TABLE books (id INTEGER PRIMARY KEY AUTOINCREMENT, title STRING NOT NULL, subTitle STRING, authors STRING, publishDate YEAR NOT NULL)");
    }

    private static void loadData(Connection connection) throws SQLException {
        Statement statement = connection.createStatement();

        statement.executeUpdate("INSERT INTO books VALUES(1, 'Agile Software Development', 'Principles, Patterns, and Practices', 'Robert C. Martin', '2003')");
        statement.executeUpdate("INSERT INTO books VALUES(2, 'Design Patterns', 'Elements of Reusable Object-Oriented Software', 'Erich Gamma,Richard Helm,Ralph Johnson,John Vlissides', '1995')");
    }
}
