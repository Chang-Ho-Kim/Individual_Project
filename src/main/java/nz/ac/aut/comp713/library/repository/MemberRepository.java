package nz.ac.aut.comp713.library.repository;

import nz.ac.aut.comp713.library.domain.Member;

import jakarta.annotation.Resource;
import jakarta.enterprise.context.ApplicationScoped;
import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class MemberRepository {

    @Resource(lookup = "jdbc/LibraryDB")
    private DataSource dataSource;

    public List<Member> findAll() {
        List<Member> members = new ArrayList<>();

        String sql = "SELECT id, name, email FROM members";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet result = statement.executeQuery()) {

            while (result.next()) {
                Member member = new Member(
                    result.getLong("id"),
                    result.getString("name"),
                    result.getString("email")
                );

                members.add(member);
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not retrieve members", e);
        }

        return members;
    }

    public Member findById(Long id) {
        String sql = """
            SELECT id, name, email
            FROM members
            WHERE id = ?
            """;

        try (Connection connection = dataSource.getConnection();
            PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet result = statement.executeQuery()) {
                if (result.next()) {
                    return new Member(
                        result.getLong("id"),
                        result.getString("name"),
                        result.getString("email")
                    );
                }
            }

        } catch (Exception e) {
            throw new RuntimeException("Could not find member", e);
        }

        return null;
    }
}