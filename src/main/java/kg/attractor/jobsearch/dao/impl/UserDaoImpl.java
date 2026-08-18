package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.UserDao;
import kg.attractor.jobsearch.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserDaoImpl implements UserDao {

    private static final String SELECT_WITH_ROLE =
            "SELECT u.*, r.name AS role_name " +
                    "FROM users u " +
                    "LEFT JOIN user_roles ur ON ur.user_id = u.id " +
                    "LEFT JOIN roles r ON r.id = ur.role_id ";

    private final DataSource dataSource;

    @Override
    public User save(User user) {
        String sql = "INSERT INTO users (name, surname, age, email, password, phone_number, avatar, enabled) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            if (user.getAge() != null) {
                statement.setInt(3, user.getAge());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getPassword());
            statement.setString(6, user.getPhoneNumber());
            statement.setString(7, user.getAvatar());
            statement.setBoolean(8, user.getEnabled() == null || user.getEnabled());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    user.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении пользователя", e);
        }

        return user;
    }

    @Override
    public Optional<User> findById(Long id) {
        return findOneByField("u.id", id);
    }

    @Override
    public List<User> findByName(String name) {
        String sql = SELECT_WITH_ROLE + "WHERE u.name = ?";
        List<User> users = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, name);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    users.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователей по имени", e);
        }

        return users;
    }

    @Override
    public Optional<User> findByPhoneNumber(String phoneNumber) {
        return findOneByField("u.phone_number", phoneNumber);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return findOneByField("u.email", email);
    }

    @Override
    public boolean existsByEmail(String email) {
        String sql = "SELECT COUNT(*) FROM users WHERE email = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, email);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования email", e);
        }

        return false;
    }

    private Optional<User> findOneByField(String fieldName, Object value) {
        String sql = SELECT_WITH_ROLE + "WHERE " + fieldName + " = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, value);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске пользователя", e);
        }

        return Optional.empty();
    }

    private User mapRow(ResultSet resultSet) throws SQLException {
        User user = new User();
        user.setId(resultSet.getLong("id"));
        user.setName(resultSet.getString("name"));
        user.setSurname(resultSet.getString("surname"));

        int age = resultSet.getInt("age");
        user.setAge(resultSet.wasNull() ? null : age);

        user.setEmail(resultSet.getString("email"));
        user.setPassword(resultSet.getString("password"));
        user.setPhoneNumber(resultSet.getString("phone_number"));
        user.setAvatar(resultSet.getString("avatar"));
        user.setEnabled(resultSet.getBoolean("enabled"));
        user.setAccountType(resultSet.getString("role_name"));
        return user;
    }

    @Override
    public void update(User user) {
        String sql = "UPDATE users SET name = ?, surname = ?, age = ?, phone_number = ?, avatar = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, user.getName());
            statement.setString(2, user.getSurname());
            if (user.getAge() != null) {
                statement.setInt(3, user.getAge());
            } else {
                statement.setNull(3, java.sql.Types.INTEGER);
            }
            statement.setString(4, user.getPhoneNumber());
            statement.setString(5, user.getAvatar());
            statement.setLong(6, user.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении пользователя", e);
        }
    }
}