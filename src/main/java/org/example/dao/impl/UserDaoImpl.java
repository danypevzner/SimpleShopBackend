package org.example.dao.impl;

import org.example.config.HibernateConfiguration;
import org.example.dao.UserDao;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class UserDaoImpl implements UserDao {

    private static final Logger logger = LoggerFactory.getLogger(UserDaoImpl.class);


    @Override
    public int createUser(User user) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(user);
            tx.commit();
            return user.getUser_id();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public User getUserById(int user_id) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            User user = session.get(User.class,user_id);
            if (user == null){
                logger.warn("User with id = {} not found",user_id);
            }
            tx.commit();
            return user;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<User> getAllUsers() {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            List<User> users = session.createQuery("from User").list();
            tx.commit();
            return users;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean alterUser(User user) {
        try (Session session = HibernateConfiguration.getFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            User existing = session.get(User.class, user.getUser_id()); // используем session.get() вместо getUserById
            if (existing == null) {
                logger.warn("User with id = {} not found", user.getUser_id());
                tx.rollback();
                return false;
            }
            session.merge(user);
            tx.commit();
            return true;
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }


    }

    @Override
    public boolean removeUser(int id) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
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
