package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.VacancyDao;
import kg.attractor.jobsearch.model.Vacancy;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class VacancyDaoImpl implements VacancyDao {

    private final DataSource dataSource;

    @Override
    public Vacancy save(Vacancy vacancy) {
        String sql = "INSERT INTO vacancies (name, description, category_id, salary, exp_from, exp_to, " +
                "is_active, author_id, created_date, update_time) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setString(1, vacancy.getName());
            statement.setString(2, vacancy.getDescription());
            statement.setLong(3, vacancy.getCategoryId());
            statement.setDouble(4, vacancy.getSalary());
            statement.setInt(5, vacancy.getExpFrom());
            statement.setInt(6, vacancy.getExpTo());
            statement.setBoolean(7, Boolean.TRUE.equals(vacancy.getIsActive()));
            statement.setLong(8, vacancy.getAuthorId());
            statement.setTimestamp(9, Timestamp.valueOf(LocalDateTime.now()));
            statement.setTimestamp(10, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    vacancy.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении вакансии", e);
        }

        return vacancy;
    }

    @Override
    public void update(Vacancy vacancy) {
        String sql = "UPDATE vacancies SET name = ?, description = ?, category_id = ?, salary = ?, " +
                "exp_from = ?, exp_to = ?, is_active = ?, update_time = ? WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, vacancy.getName());
            statement.setString(2, vacancy.getDescription());
            statement.setLong(3, vacancy.getCategoryId());
            statement.setDouble(4, vacancy.getSalary());
            statement.setInt(5, vacancy.getExpFrom());
            statement.setInt(6, vacancy.getExpTo());
            statement.setBoolean(7, Boolean.TRUE.equals(vacancy.getIsActive()));
            statement.setTimestamp(8, Timestamp.valueOf(LocalDateTime.now()));
            statement.setLong(9, vacancy.getId());

            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении вакансии", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM vacancies WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении вакансии", e);
        }
    }

    @Override
    public Optional<Vacancy> findById(Long id) {
        String sql = "SELECT * FROM vacancies WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске вакансии по id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Vacancy> findAll() {
        return findAllByField(null, null);
    }

    @Override
    public List<Vacancy> findByAuthorId(Long authorId) {
        return findAllByField("author_id", authorId);
    }

    @Override
    public List<Vacancy> findByCategoryId(Long categoryId) {
        return findAllByField("category_id", categoryId);
    }

    private List<Vacancy> findAllByField(String fieldName, Object value) {
        String sql = "SELECT * FROM vacancies" + (fieldName != null ? " WHERE " + fieldName + " = ?" : "")
                + " ORDER BY update_time DESC";
        List<Vacancy> vacancies = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (fieldName != null) {
                statement.setObject(1, value);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    vacancies.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка вакансий", e);
        }

        return vacancies;
    }

    private Vacancy mapRow(ResultSet resultSet) throws SQLException {
        Vacancy vacancy = new Vacancy();
        vacancy.setId(resultSet.getLong("id"));
        vacancy.setName(resultSet.getString("name"));
        vacancy.setDescription(resultSet.getString("description"));
        vacancy.setCategoryId(resultSet.getLong("category_id"));
        vacancy.setSalary(resultSet.getDouble("salary"));
        vacancy.setExpFrom(resultSet.getInt("exp_from"));
        vacancy.setExpTo(resultSet.getInt("exp_to"));
        vacancy.setIsActive(resultSet.getBoolean("is_active"));
        vacancy.setAuthorId(resultSet.getLong("author_id"));

        Timestamp createdDate = resultSet.getTimestamp("created_date");
        vacancy.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);

        Timestamp updateTime = resultSet.getTimestamp("update_time");
        vacancy.setUpdateTime(updateTime != null ? updateTime.toLocalDateTime() : null);

        return vacancy;
    }
}