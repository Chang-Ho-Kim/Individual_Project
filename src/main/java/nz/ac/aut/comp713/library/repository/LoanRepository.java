package nz.ac.aut.comp713.library.repository;

import nz.ac.aut.comp713.library.domain.Loan;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class LoanRepository {

    @Resource(lookup = "jdbc/LibraryDB")
    private DataSource dataSource;

    public List<Loan> findAll() {
        List<Loan> loans = new ArrayList<>();

        String sql = """
            SELECT id, member_id, book_id, borrowed_at, returned_at
            FROM loans
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                loans.add(mapLoan(result));
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve loans", e);
        }

        return loans;
    }

    public boolean hasActiveLoan(Long bookId) {
        String sql = """
            SELECT COUNT(*)
            FROM loans
            WHERE book_id = ?
            AND returned_at IS NULL
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, bookId);

            try (ResultSet result = statement.executeQuery()) {
                result.next();
                return result.getInt(1) > 0;
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not check book loan", e);
        }
    }

    public Loan create(Long memberId, Long bookId) {
        String sql = """
            INSERT INTO loans (member_id, book_id, borrowed_at)
            VALUES (?, ?, CURRENT_TIMESTAMP)
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                 sql,
                 java.sql.Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, memberId);
            statement.setLong(2, bookId);

            statement.executeUpdate();

            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    Long id = keys.getLong(1);

                    return new Loan(
                        id,
                        memberId,
                        bookId,
                        LocalDateTime.now(),
                        null
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not create loan", e);
        }

        throw new RuntimeException("Could not create loan");
    }

    public Loan findActiveLoan(Long loanId) {
        String sql = """
            SELECT id, member_id, book_id, borrowed_at, returned_at
            FROM loans
            WHERE id = ?
            AND returned_at IS NULL
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, loanId);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return mapLoan(result);
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not find loan", e);
        }

        return null;
    }

    public void returnLoan(Long loanId) {
        String sql = """
            UPDATE loans
            SET returned_at = CURRENT_TIMESTAMP
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, loanId);
            statement.executeUpdate();

        } catch (Exception e) {
            throw new RuntimeException("Could not return loan", e);
        }
    }

    private Loan mapLoan(ResultSet result) throws Exception {
        Timestamp borrowed = result.getTimestamp("borrowed_at");
        Timestamp returned = result.getTimestamp("returned_at");

        return new Loan(
            result.getLong("id"),
            result.getLong("member_id"),
            result.getLong("book_id"),
            borrowed != null ? borrowed.toLocalDateTime() : null,
            returned != null ? returned.toLocalDateTime() : null
        );
    }

    public List<Loan> findByMemberId(Long memberId) {

        List<Loan> loans = new ArrayList<>();

        String sql = """
            SELECT id, member_id, book_id, borrowed_at, returned_at
            FROM loans
            WHERE member_id = ?
            ORDER BY borrowed_at DESC
            """;

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, memberId);

            try (ResultSet result = statement.executeQuery()) {

                while (result.next()) {
                    loans.add(mapLoan(result));
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve member loans", e);
        }

        return loans;
    }

}