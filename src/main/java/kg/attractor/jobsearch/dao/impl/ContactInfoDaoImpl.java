package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.ContactInfoDao;
import kg.attractor.jobsearch.model.ContactInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class ContactInfoDaoImpl implements ContactInfoDao {

    private final DataSource dataSource;

    @Override
    public ContactInfo save(ContactInfo contactInfo) {
        String sql = "INSERT INTO contacts_info (type_id, resume_id, contact_value) VALUES (?, ?, ?)";
        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, contactInfo.getTypeId());
            statement.setLong(2, contactInfo.getResumeId());
            statement.setString(3, contactInfo.getValue());

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    contactInfo.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении контакта", e);
        }

        return contactInfo;
    }

    @Override
    public List<ContactInfo> findByResumeId(Long resumeId) {
        String sql = "SELECT * FROM contacts_info WHERE resume_id = ?";
        List<ContactInfo> contacts = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, resumeId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    contacts.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении контактов резюме", e);
        }

        return contacts;
    }

    @Override
    public void deleteByResumeId(Long resumeId) {
        String sql = "DELETE FROM contacts_info WHERE resume_id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, resumeId);
            statement.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при удалении контактов резюме", e);
        }
    }

    private ContactInfo mapRow(ResultSet resultSet) throws SQLException {
        ContactInfo contactInfo = new ContactInfo();
        contactInfo.setId(resultSet.getLong("id"));
        contactInfo.setTypeId(resultSet.getLong("type_id"));
        contactInfo.setResumeId(resultSet.getLong("resume_id"));
        contactInfo.setValue(resultSet.getString("contact_value"));        return contactInfo;
    }
}