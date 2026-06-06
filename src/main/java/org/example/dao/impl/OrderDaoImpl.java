package org.example.dao.impl;

import org.example.config.HibernateConfiguration;
import org.example.dao.OrderDao;
import org.example.model.Order;
import org.example.model.User;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import java.math.BigDecimal;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private static Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);


    @Override
    public int createOrder(Order order) {
        try ( Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(order);
            tx.commit();
            return order.getOrder_id();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Order getOrderByID(int order_id) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Order object = session.get(Order.class,order_id);
            tx.commit();
            return object;

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> results;
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            results =  session.createQuery("FROM Order",Order.class).getResultList();
            tx.commit();
            return results;

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getOrdersByUser(User user) {
        List<Order> results;
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Query<Order> query = session.createQuery("FROM Order where user = :user",Order.class);
            query.setParameter("user",user);
            tx.commit();
            results = query.list();
            return results;

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateStatus(int order_id, String status) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Order order = getOrderByID(order_id);
            if (order == null) {
                logger.warn("Order with ID {} not found", order_id);
                return false; // Заказ не найден
            }
            order.setStatus(status);
            session.merge(order);
            tx.commit();
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteOrderByID(int order_id) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Order order = getOrderByID(order_id);
            if (order == null) {
                logger.warn("Order with ID {} not found", order_id);
                return false; // Заказ не найден
            }
            session.remove(order);
            tx.commit();
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateTotalPrice(int order_id, BigDecimal new_total_price) {

        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Order order = getOrderByID(order_id);
            if (order == null) {
                logger.warn("Order with ID {} not found", order_id);
                return false;
            }
            order.setTotalPrice(new_total_price);
            session.update(order);
            tx.commit();
            return true;
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }
}
