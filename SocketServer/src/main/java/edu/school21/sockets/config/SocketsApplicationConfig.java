package edu.school21.sockets.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import edu.school21.sockets.repository.chatroom.ChatroomRowMapper;
import edu.school21.sockets.repository.message.MessageRowMapper;
import edu.school21.sockets.repository.user.UserRowMapper;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;

import javax.sql.DataSource;
@Configuration
@PropertySource("classpath:db.properties")
@ComponentScan(basePackages = {"edu.school21.sockets"})
public class SocketsApplicationConfig {

    @Value("${db.url}")
    String url;

    @Value("${db.user}")
    String user;

    @Value("${db.password}")
    String password;

    @Value("${db.driver.name}")
    String driverName;

    @Bean
    public HikariConfig hikariConfig() {
        HikariConfig hikariConfig = new HikariConfig();
        hikariConfig.setJdbcUrl(url);
        hikariConfig.setUsername(user);
        hikariConfig.setPassword(password);
        return hikariConfig;
    }

    @Bean
    public DataSource hikariDataSource() {
        return new HikariDataSource(hikariConfig());
    }

    @Bean
    public NamedParameterJdbcTemplate namedParameterJdbcTemplate() {
        return new NamedParameterJdbcTemplate(hikariDataSource());
    }

    @Bean
    public UserRowMapper userRowMapper() {
        return new UserRowMapper();
    }

    @Bean
    public MessageRowMapper messageRowMapper() {
        return new MessageRowMapper(userRowMapper(), chatroomRowMapper());
    }

    @Bean
    public ChatroomRowMapper chatroomRowMapper() {
        return new ChatroomRowMapper();
    }

}
