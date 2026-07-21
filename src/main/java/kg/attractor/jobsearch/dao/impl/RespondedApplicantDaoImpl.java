package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.RespondedApplicantDao;
import kg.attractor.jobsearch.model.RespondedApplicant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class RespondedApplicantDaoImpl implements RespondedApplicantDao {

    private final DataSource dataSource;

    @Override
    public RespondedApplicant save(RespondedApplicant respondedApplicant) {
        String sql = "INSERT INTO responded_applicants (resume_id, vacancy_id, confirmation) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, respondedApplicant.getResumeId());
            statement.setLong(2, respondedApplicant.getVacancyId());
            statement.setBoolean(3, Boolean.TRUE.equals(respondedApplicant.getConfirmation()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    respondedApplicant.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении отклика", e);
        }

        return respondedApplicant;
    }

    @Override
    public Optional<RespondedApplicant> findById(Long id) {
        return findAllByField("id", id).stream().findFirst();
    }

    @Override
    public List<RespondedApplicant> findByVacancyId(Long vacancyId) {
        return findAllByField("vacancy_id", vacancyId);
    }

    @Override
    public List<RespondedApplicant> findByResumeId(Long resumeId) {
        return findAllByField("resume_id", resumeId);
    }

    @Override
    public boolean existsByResumeIdAndVacancyId(Long resumeId, Long vacancyId) {
        String sql = "SELECT COUNT(*) FROM responded_applicants WHERE resume_id = ? AND vacancy_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, resumeId);
            statement.setLong(2, vacancyId);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return resultSet.getInt(1) > 0;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при проверке существования отклика", e);
        }

        return false;
    }

    private List<RespondedApplicant> findAllByField(String fieldName, Object value) {
        String sql = "SELECT * FROM responded_applicants WHERE " + fieldName + " = ?";
        List<RespondedApplicant> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setObject(1, value);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении откликов", e);
        }

        return result;
    }

    private RespondedApplicant mapRow(ResultSet resultSet) throws SQLException {
        RespondedApplicant respondedApplicant = new RespondedApplicant();
        respondedApplicant.setId(resultSet.getLong("id"));
        respondedApplicant.setResumeId(resultSet.getLong("resume_id"));
        respondedApplicant.setVacancyId(resultSet.getLong("vacancy_id"));
        respondedApplicant.setConfirmation(resultSet.getBoolean("confirmation"));
        return respondedApplicant;
    }
}