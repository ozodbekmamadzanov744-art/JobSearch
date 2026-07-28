package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.ResumeDao;
import kg.attractor.jobsearch.model.Resume;
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
public class ResumeDaoImpl implements ResumeDao {

    private final DataSource dataSource;

    @Override
    public Resume save(Resume resume) {
        String sql = "INSERT INTO resumes (applicant_id, name, category_id, salary, is_active, created_date, update_time) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, resume.getApplicantId());
            statement.setString(2, resume.getName());
            statement.setLong(3, resume.getCategoryId());
            statement.setDouble(4, resume.getSalary());
            statement.setBoolean(5, Boolean.TRUE.equals(resume.getIsActive()));
            LocalDateTime now = LocalDateTime.now();
            statement.setTimestamp(6, Timestamp.valueOf(now));
            statement.setTimestamp(7, Timestamp.valueOf(now));

            statement.executeUpdate();

            resume.setCreatedDate(now);
            resume.setUpdateTime(now);

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    resume.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении резюме", e);
        }

        return resume;
    }

    @Override
    public void update(Resume resume) {
        String sql = "UPDATE resumes SET name = ?, category_id = ?, salary = ?, is_active = ?, update_time = ? " +
                "WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setString(1, resume.getName());
            statement.setLong(2, resume.getCategoryId());
            statement.setDouble(3, resume.getSalary());
            statement.setBoolean(4, Boolean.TRUE.equals(resume.getIsActive()));
            LocalDateTime now = LocalDateTime.now();
            statement.setTimestamp(5, Timestamp.valueOf(now));
            statement.setLong(6, resume.getId());

            statement.executeUpdate();

            resume.setUpdateTime(now);
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при обновлении резюме", e);
        }
    }

    @Override
    public void delete(Long id) {
        String sql = "DELETE FROM resumes WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении резюме", e);
        }
    }

    @Override
    public Optional<Resume> findById(Long id) {
        String sql = "SELECT * FROM resumes WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске резюме по id", e);
        }

        return Optional.empty();
    }

    @Override
    public List<Resume> findAll() {
        return findAllByField(null, null);
    }

    @Override
    public List<Resume> findByApplicantId(Long applicantId) {
        return findAllByField("applicant_id", applicantId);
    }

    @Override
    public List<Resume> findByCategoryId(Long categoryId) {
        return findAllByField("category_id", categoryId);
    }

    private List<Resume> findAllByField(String fieldName, Object value) {
        String sql = "SELECT * FROM resumes" + (fieldName != null ? " WHERE " + fieldName + " = ?" : "");
        List<Resume> resumes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            if (fieldName != null) {
                statement.setObject(1, value);
            }

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    resumes.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении списка резюме", e);
        }

        return resumes;
    }

    private Resume mapRow(ResultSet resultSet) throws SQLException {
        Resume resume = new Resume();
        resume.setId(resultSet.getLong("id"));
        resume.setApplicantId(resultSet.getLong("applicant_id"));
        resume.setName(resultSet.getString("name"));
        resume.setCategoryId(resultSet.getLong("category_id"));
        resume.setSalary(resultSet.getDouble("salary"));
        resume.setIsActive(resultSet.getBoolean("is_active"));

        Timestamp createdDate = resultSet.getTimestamp("created_date");
        resume.setCreatedDate(createdDate != null ? createdDate.toLocalDateTime() : null);

        Timestamp updateTime = resultSet.getTimestamp("update_time");
        resume.setUpdateTime(updateTime != null ? updateTime.toLocalDateTime() : null);

        return resume;
    }
}