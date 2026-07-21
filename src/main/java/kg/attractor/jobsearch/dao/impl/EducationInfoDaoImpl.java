package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.EducationInfoDao;
import kg.attractor.jobsearch.model.EducationInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class EducationInfoDaoImpl implements EducationInfoDao {

    private final DataSource dataSource;

    @Override
    public EducationInfo save(EducationInfo educationInfo) {
        String sql = "INSERT INTO education_info (resume_id, institution, program, start_date, end_date, degree) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, educationInfo.getResumeId());
            statement.setString(2, educationInfo.getInstitution());
            statement.setString(3, educationInfo.getProgram());
            statement.setDate(4, educationInfo.getStartDate() != null ? Date.valueOf(educationInfo.getStartDate()) : null);
            statement.setDate(5, educationInfo.getEndDate() != null ? Date.valueOf(educationInfo.getEndDate()) : null);
            statement.setString(6, educationInfo.getDegree());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    educationInfo.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении записи об образовании", e);
        }

        return educationInfo;
    }

    @Override
    public List<EducationInfo> findByResumeId(Long resumeId) {
        String sql = "SELECT * FROM education_info WHERE resume_id = ?";
        List<EducationInfo> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, resumeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении записей об образовании", e);
        }

        return result;
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        String sql = "DELETE FROM education_info WHERE resume_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, resumeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении записей об образовании", e);
        }
    }

    private EducationInfo mapRow(ResultSet resultSet) throws SQLException {
        EducationInfo educationInfo = new EducationInfo();
        educationInfo.setId(resultSet.getLong("id"));
        educationInfo.setResumeId(resultSet.getLong("resume_id"));
        educationInfo.setInstitution(resultSet.getString("institution"));
        educationInfo.setProgram(resultSet.getString("program"));

        Date startDate = resultSet.getDate("start_date");
        educationInfo.setStartDate(startDate != null ? startDate.toLocalDate() : null);

        Date endDate = resultSet.getDate("end_date");
        educationInfo.setEndDate(endDate != null ? endDate.toLocalDate() : null);

        educationInfo.setDegree(resultSet.getString("degree"));
        return educationInfo;
    }
}
