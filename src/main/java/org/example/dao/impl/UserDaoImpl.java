package org.example.dao.impl;

import org.example.config.DBConnection;
import org.example.dao.UserDao;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final DBConnection dbConnection;
    private static final String INSERT_SQL = "INSERT INTO shop.user (name,email, password_hash,created_At) VALUES (?, ?, ?,?)";
    private static final String SELECT_BY_ID_SQL = "select user_id, name,email, password_hash,created_At from shop.user where user_id = ?";
    private static final String SELECT_ALL_SQL = "select user_id, name,email, password_hash,created_At from shop.user";
    private static final String UPDATE_SQL = "UPDATE shop.user set name = ?, email = ?,password_hash = ?, created_At = ? where user_id = ?";
    private static final String DELETE_BY_ID_SQL = "delete from shop.user where user_id = ?";
    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);

    public UserDaoImpl() {
        try {
            this.dbConnection = DBConnection.getInstance();
            logger.info("archived DBConnection");
        }
        catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public int createUser(User user) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL,Statement.RETURN_GENERATED_KEYS)){
            //statement.setInt(1,user.getUser_id());
            statement.setString(1, user.getName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPasswordHash());
            statement.setTimestamp(4, Timestamp.valueOf(user.getCreatedAt()));
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            logger.error("Failed to retrieve generated order_id");
            throw new RuntimeException("Failed to retrieve generated order_id");
        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(int user_id) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, user_id);
            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    return new User(rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getTimestamp("created_At").toLocalDateTime());
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            // логирование + проброс
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        Connection connection = dbConnection.getConnection();
        List<User> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL)) {
            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    User user = new User(rs.getInt("user_id"),
                            rs.getString("name"),
                            rs.getString("email"),
                            rs.getString("password_hash"),
                            rs.getTimestamp("created_At").toLocalDateTime());
                    results.add(user);
                }
            }
        } catch (SQLException e) {
            // логирование + проброс
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public boolean alterUser(User user) {
        Connection connection = dbConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);

            statement.setString(1,user.getName());
            statement.setString(2,user.getEmail());
            statement.setString(3,user.getPasswordHash());
            statement.setTimestamp(4,Timestamp.valueOf(user.getCreatedAt()));
            statement.setInt(5,user.getUser_id());
            int rows = statement.executeUpdate();
            return rows>0;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeUser(int id) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            return rows>0;
        } catch (SQLException e) {
            // логирование + проброс
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
