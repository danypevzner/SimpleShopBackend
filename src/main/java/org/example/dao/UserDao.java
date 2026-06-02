package org.example.dao;

import org.example.model.User;

import java.util.List;

public interface UserDao {
     int createUser(User user);
     User getUserById(int user_id);
     List<User> getAllUsers();
     boolean alterUser(User user);
     boolean removeUser(int id);
}
