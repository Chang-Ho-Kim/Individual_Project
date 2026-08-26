package nz.ac.aut.comp713.library.repository;

import nz.ac.aut.comp713.library.domain.Book;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class BookRepository {

    @Resource(lookup = "jdbc/LibraryDB")
    private DataSource dataSource;

    public List<Book> findAll() {
        List<Book> books = new ArrayList<>();

        String sql = "SELECT id, title, author, isbn, available FROM books";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Book book = new Book(
                    result.getLong("id"),
                    result.getString("title"),
                    result.getString("author"),
                    result.getString("isbn"),
                    result.getBoolean("available")
                );

                books.add(book);
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve books", e);
        }

        return books;
    }

    public Book findById(Long id) {
        String sql = """
            SELECT id, title, author, isbn, available
            FROM books
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Book(
                        result.getLong("id"),
                        result.getString("title"),
                        result.getString("author"),
                        result.getString("isbn"),
                        result.getBoolean("available")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not find book", e);
        }

        return null;
    }

    public void setAvailability(Long bookId, boolean available) {
        String sql = """
            UPDATE books
            SET available = ?
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBoolean(1, available);
            statement.setLong(2, bookId);

            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Could not update book availability", e);
        }
    }
}