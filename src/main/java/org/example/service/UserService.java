package org.example.service;

import org.example.dao.UserDao;
import org.example.model.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


import java.time.LocalDateTime;
import java.util.List;

public class UserService {
    public final UserDao userDao;
    Logger logger = LoggerFactory.getLogger(UserService.class);
    public UserService(UserDao dao) {
        this.userDao = dao;
    }

        public int createUser(String name, String email, String passwordHash, LocalDateTime creationTime) {
            User user = new User(name,email,passwordHash,creationTime);
            if ((user.getName().length()>45)||(user.getName().length()==0)){
                logger.warn("Failed to create user- incorrect name length");
                throw new IllegalArgumentException("user name has incorrect length");
            }
            if (!user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")){
                logger.warn("Failed to create user- incorrect email format");
                throw new IllegalArgumentException("email format incorrect");
            }
            int user_id = userDao.createUser(user);
            logger.info("User created with id ="+user_id);
            return user_id;

        }



        public User getUserById(int user_id) {
            if (user_id<0){
                logger.warn("Failed to get user - userId<0");
                throw new IllegalArgumentException("user_id<0");
            }
            User user =userDao.getUserById(user_id);
            ensureExisting(user);
            logger.info("User with id ="+user_id+" found");
            return user;
        }


        public List<User> getAllUsers() {
            logger.info("All users found");
            return userDao.getAllUsers();

        }


        public boolean alterUser(int user_id,String name, String email, String passwordHash, LocalDateTime creationTime) {
        User user = new User(name,email,passwordHash,creationTime);
            if (user.getUser_id()<0){
                logger.warn("Failed to alter user - userId<0");
                throw new IllegalArgumentException("user_id<0");
            }
            if ((user.getName().length()>45)||(user.getName().length()==0)){
                logger.warn("Failed to alter user - incorrect username langth");
                throw new IllegalArgumentException("user name has incorrect length");
            }
            if (!user.getEmail().matches("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$")||(user.getEmail().length()>100)){
                logger.warn("Failed to alter user - incorrect email format");
                throw new IllegalArgumentException("email format incorrect");
            }
            User existing = userDao.getUserById(user_id);
            ensureExisting(existing);
            logger.info("user with id = "+user_id+" changed profile");
            return userDao.alterUser(user);
        }


        public boolean removeUser(int user_id) {
            if (user_id<0){
                logger.warn("Failed to remove user - user_id<0");
                throw new IllegalArgumentException("user_id<0");
            }
            User user =userDao.getUserById(user_id);
            ensureExisting(user);
            logger.info("user with id = "+user_id+" removed");
            return userDao.removeUser(user_id);

        }

        private void ensureExisting(User user){
            if (user == null){
                logger.warn("User not found ");
                throw new RuntimeException("User not found");
            }
        }

}
