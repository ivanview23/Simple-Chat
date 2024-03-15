package edu.school21.sockets.repository.chatroom;

import edu.school21.sockets.models.Chatroom;
import edu.school21.sockets.models.User;
import edu.school21.sockets.repository.user.UserRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

public class ChatroomRowMapper implements RowMapper<Chatroom> {

    @Override
    public Chatroom mapRow(ResultSet resultSet, int rowNum) throws SQLException {
        long id = resultSet.getLong("id");
        String room = resultSet.getString("room");
        return new Chatroom(id, room);
    }
}