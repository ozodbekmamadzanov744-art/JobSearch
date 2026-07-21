package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.WorkExperienceInfoDao;
import kg.attractor.jobsearch.model.WorkExperienceInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class WorkExperienceInfoDaoImpl implements WorkExperienceInfoDao {

    private final DataSource dataSource;

    @Override
    public WorkExperienceInfo save(WorkExperienceInfo workExperienceInfo) {
        String sql = "INSERT INTO work_experience_info (resume_id, years, company_name, position, responsibilities) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, workExperienceInfo.getResumeId());
            statement.setInt(2, workExperienceInfo.getYears());
            statement.setString(3, workExperienceInfo.getCompanyName());
            statement.setString(4, workExperienceInfo.getPosition());
            statement.setString(5, workExperienceInfo.getResponsibilities());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    workExperienceInfo.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении записи об опыте работы", e);
        }

        return workExperienceInfo;
    }

    @Override
    public List<WorkExperienceInfo> findByResumeId(Long resumeId) {
        String sql = "SELECT * FROM work_experience_info WHERE resume_id = ?";
        List<WorkExperienceInfo> result = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, resumeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    result.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении записей об опыте работы", e);
        }

        return result;
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        String sql = "DELETE FROM work_experience_info WHERE resume_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, resumeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении записей об опыте работы", e);
        }
    }

    private WorkExperienceInfo mapRow(ResultSet resultSet) throws SQLException {
        WorkExperienceInfo workExperienceInfo = new WorkExperienceInfo();
        workExperienceInfo.setId(resultSet.getLong("id"));
        workExperienceInfo.setResumeId(resultSet.getLong("resume_id"));
        workExperienceInfo.setYears(resultSet.getInt("years"));
        workExperienceInfo.setCompanyName(resultSet.getString("company_name"));
        workExperienceInfo.setPosition(resultSet.getString("position"));
        workExperienceInfo.setResponsibilities(resultSet.getString("responsibilities"));
        return workExperienceInfo;
    }
}