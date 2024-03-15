package edu.school21.sockets.repository.user;

import edu.school21.sockets.models.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;

@Component
public class UserRepositoryImpl implements UserRepository<User> {

    private final DataSource dataSource;
    private final UserRowMapper userRowMapper;
    private final NamedParameterJdbcTemplate namedParameterJdbcTemplate;
    private final PasswordEncoder passwordEncoder;
    private final Map<String, Object> parameters;


    @Autowired
    public UserRepositoryImpl(DataSource dataSource, UserRowMapper userRowMapper, NamedParameterJdbcTemplate namedParameterJdbcTemplate) {
        this.dataSource = dataSource;
        this.userRowMapper = userRowMapper;
        this.namedParameterJdbcTemplate = namedParameterJdbcTemplate;
        passwordEncoder = new BCryptPasswordEncoder();
        parameters = new HashMap<>();
    }

    @Override
    public void createBase() {
        try {
            Connection connection = dataSource.getConnection();
            Statement statement = connection.createStatement();
            statement.execute("DROP TABLE IF EXISTS client CASCADE;\n" +
                    "CREATE TABLE client (id SERIAL PRIMARY KEY, " +
                    "username VARCHAR, password VARCHAR);\n");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void save(User user) {
            String command = "INSERT INTO client (username, password) VALUES ( :username, :password)";
            String codePassword = passwordEncoder.encode(user.getPassword());

            parameters.clear();
            parameters.put("username", user.getName());
            parameters.put("password", codePassword);

            namedParameterJdbcTemplate.update(command, parameters);
    }

    @Override
    public Optional<User> findByName(String username) {
        String command = "SELECT * FROM client WHERE username = :username";

        parameters.clear();
        parameters.put("username", username);

        List<User> userList = namedParameterJdbcTemplate.query(command, parameters, userRowMapper);
        return userList.isEmpty() ? Optional.empty() : Optional.of(userList.get(0));
    }

    @Override
    public boolean verification(String name, String password) {
        String currentPassword = findByName(name).get().getPassword();
        return passwordEncoder.matches(password, currentPassword);
    }

    @Override
    public List<User> findAll(long id) {
        return null;
    }

    @Override
    public void update(User entity) {

    }

    @Override
    public void delete(Long id) {

    }

}
