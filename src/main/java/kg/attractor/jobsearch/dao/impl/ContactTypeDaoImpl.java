package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.ContactTypeDao;
import kg.attractor.jobsearch.model.ContactType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class ContactTypeDaoImpl implements ContactTypeDao {

    private final DataSource dataSource;

    @Override
    public List<ContactType> findAll() {
        String sql = "SELECT * FROM contact_types";
        List<ContactType> contactTypes = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                contactTypes.add(mapRow(resultSet));
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении типов контактов", e);
        }

        return contactTypes;
    }

    @Override
    public Optional<ContactType> findById(Long id) {
        String sql = "SELECT * FROM contact_types WHERE id = ?";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, id);

            try (ResultSet resultSet = statement.executeQuery()) {
                if (resultSet.next()) {
                    return Optional.of(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при поиске типа контакта по id", e);
        }

        return Optional.empty();
    }

    private ContactType mapRow(ResultSet resultSet) throws SQLException {
        ContactType contactType = new ContactType();
        contactType.setId(resultSet.getLong("id"));
        contactType.setType(resultSet.getString("type"));
        return contactType;
    }
}