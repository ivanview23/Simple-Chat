package edu.school21.sockets.repository.message;

import edu.school21.sockets.models.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
public class MessageRepositoryImpl implements MessageRepository<Message> {

    private final DataSource dataSource;
    private final MessageRowMapper messageRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Map<String, Object> parameters;

    @Autowired
    public MessageRepositoryImpl(DataSource dataSource, MessageRowMapper messageRowMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.dataSource = dataSource;
        this.messageRowMapper = messageRowMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        parameters = new HashMap<>();
    }
    @Override
    public void createBase() {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS message CASCADE;\n" +
                    "CREATE TABLE message (id SERIAL PRIMARY KEY," +
                    " author INT REFERENCES client (id) NOT NULL," +
                    " chatroom INT REFERENCES chatroom (id) NOT NULL," +
                    " text VARCHAR(1000), date TIMESTAMP);\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Message entity) {
            String command = "INSERT INTO message (author, chatroom, text, date) VALUES ( :author, :chatroom, :text, :date)";

            parameters.clear();
            parameters.put("author", entity.getUser().getId());
            parameters.put("chatroom", entity.getChatroom().getId());
            parameters.put("text", entity.getText());
            parameters.put("date", entity.getDate());
            namedParameterJdbcTemplate.update(command, parameters);
    }

    @Override
    public Optional<Message> findByName(String name) {
        return Optional.empty();
    }

    @Override
    public boolean verification(String name, String password) {
        return false;
    }

    @Override
    public List<Message> findAll(long id) {
        String command = "SELECT * FROM message AS m\n" +
                "JOIN client AS u ON m.author = u.id\n" +
                "JOIN chatroom AS ch ON m.chatroom = ch.id\n" +
                "WHERE chatroom = :id LIMIT 30;";

        parameters.clear();
        parameters.put("id", id);

        return namedParameterJdbcTemplate.query(command, parameters, messageRowMapper);
    }

    @Override
    public void update(Message entity) {

    }

    @Override
    public void delete(Long id) {

    }
}
