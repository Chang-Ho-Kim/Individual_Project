package nz.ac.aut.comp713.library.repository;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@ApplicationScoped
public class AuthRepository {

    @Resource(lookup = "jdbc/LibraryDB")
    private DataSource dataSource;

    public AuthUser findByEmail(String email) {

        String sql = """
            SELECT id, name, email, password
            FROM members
            WHERE email = ?
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet result = statement.executeQuery()) {

                if (result.next()) {
                    return new AuthUser(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("email"),
                        result.getString("password")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException(
                "Could not find user for authentication",
                e
            );
        }

        return null;
    }
}