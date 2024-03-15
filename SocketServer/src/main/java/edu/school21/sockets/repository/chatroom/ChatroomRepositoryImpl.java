package edu.school21.sockets.repository.chatroom;

import edu.school21.sockets.models.Chatroom;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
public class ChatroomRepositoryImpl implements ChatroomRepository<Chatroom> {

    private final DataSource dataSource;
    private final ChatroomRowMapper chatroomRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final Map<String, Object> parameters;

    @Autowired
    public ChatroomRepositoryImpl (DataSource dataSource, ChatroomRowMapper chatroomRowMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.dataSource = dataSource;
        this.chatroomRowMapper = chatroomRowMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        parameters = new HashMap<>();
    }

    @Override
    public void createBase() {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS chatroom CASCADE;\n" +
                    "CREATE TABLE chatroom (id SERIAL PRIMARY KEY," +
                    " room VARCHAR);\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(Chatroom entity) {
            String command = "INSERT INTO chatroom (room) VALUES ( :room)";

            parameters.clear();
            parameters.put("room", entity.getName());

            namedParameterJdbcTemplate.update(command, parameters);
    }

    @Override
    public Optional<Chatroom> findByName(String room) {
            String command = "SELECT * FROM chatroom WHERE room = :room";

            parameters.clear();
            parameters.put("room", room);

            List<Chatroom> chatroomList = namedParameterJdbcTemplate.query(command, parameters, chatroomRowMapper);
            return chatroomList.isEmpty() ? Optional.empty() : Optional.of(chatroomList.get(0));
    }

    @Override
    public boolean verification(String name, String password) {
        return false;
    }

    @Override
    public List<Chatroom> findAll(long id) {
        String command = "SELECT * FROM chatroom";
        return namedParameterJdbcTemplate.query(command, parameters, chatroomRowMapper);
    }

    @Override
    public void update(Chatroom entity) {

    }

    @Override
    public void delete(Long id) {

    }
}
