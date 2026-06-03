package org.example.dao.impl;

import org.example.config.DBConnection;
import org.example.config.HibernateConfiguration;
import org.example.dao.UserDao;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class UserDaoImpl implements UserDao {
    private final DBConnection dbConnection;

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
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.save(user);
            tx.commit();
            return user.getUser_id();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(int user_id) {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class,user_id);
            tx.commit();
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            List<User> users = session.createQuery("from USER").list();
            tx.commit();
            return users;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean alterUser(User user) {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.update(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public boolean removeUser(int id) {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class,id);
            session.delete(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
