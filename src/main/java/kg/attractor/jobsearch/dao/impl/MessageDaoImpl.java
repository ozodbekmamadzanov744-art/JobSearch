package kg.attractor.jobsearch.dao.impl;

import kg.attractor.jobsearch.dao.MessageDao;
import kg.attractor.jobsearch.model.Message;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;
import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class MessageDaoImpl implements MessageDao {

    private final DataSource dataSource;

    @Override
    public Message save(Message message) {
        String sql = "INSERT INTO messages (responded_applicants_id, content, timestamp) VALUES (?, ?, ?)";

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            statement.setLong(1, message.getRespondedApplicantsId());
            statement.setString(2, message.getContent());
            statement.setTimestamp(3, Timestamp.valueOf(LocalDateTime.now()));

            statement.executeUpdate();

            try (ResultSet generatedKeys = statement.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    message.setId(generatedKeys.getLong(1));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при сохранении сообщения", e);
        }

        return message;
    }

    @Override
    public List<Message> findByRespondedApplicantsId(Long respondedApplicantsId) {
        String sql = "SELECT * FROM messages WHERE responded_applicants_id = ? ORDER BY timestamp ASC";
        List<Message> messages = new ArrayList<>();

        try (Connection connection = dataSource.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setLong(1, respondedApplicantsId);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    messages.add(mapRow(resultSet));
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Ошибка при получении сообщений чата", e);
        }

        return messages;
    }

    private Message mapRow(ResultSet resultSet) throws SQLException {
        Message message = new Message();
        message.setId(resultSet.getLong("id"));
        message.setRespondedApplicantsId(resultSet.getLong("responded_applicants_id"));
        message.setContent(resultSet.getString("content"));

        Timestamp timestamp = resultSet.getTimestamp("timestamp");
        message.setTimestamp(timestamp != null ? timestamp.toLocalDateTime() : null);

        return message;
    }
}