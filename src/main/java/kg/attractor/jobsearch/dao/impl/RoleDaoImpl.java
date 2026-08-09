package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.RoleDao;
import kg.attractor.jobsearch.model.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RoleDaoImpl implements RoleDao {

    private final DataSource dataSource;

    @Override
    public Optional<Role> findByName(String name) {
        String sql = "SELECT * FROM roles WHERE name = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    Role role = new Role();
                    role.setId(resultSet.getLong("id"));
                    role.setName(resultSet.getString("name"));
                    return Optional.of(role);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске роли по названию", e);
        }

        return Optional.empty();
    }
}