package org.example.dao.impl;

import org.example.config.HibernateConfiguration;
import org.example.dao.OrderItemDao;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.hibernate.query.Query;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class OrderItemDaoImpl implements OrderItemDao {

    private static  Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);

    @Override
    public int addItem(OrderItem item) {
        try(Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx= session.beginTransaction();
            session.persist(item);
            tx.commit();
            return item.getItem_id();
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public boolean removeItem(int item_id) {
        try(Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx= session.beginTransaction();
            OrderItem item  =session.get(OrderItem.class,item_id);
            if (item == null) {
                logger.warn("Item with ID {} not found", item_id);
                return false; // Item не найден
            }
            session.remove(item);
            tx.commit();
            return true;
        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public List<OrderItem> getItemsByOrder(Order order) {
        List<OrderItem> results;
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Query<OrderItem> query = session.createQuery("FROM OrderItem where order = :order",OrderItem.class);
            query.setParameter("order",order);
            tx.commit();
            results = query.list();
            return results;

        }catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteItemsByOrder(int order_id) {
        try (Session session = HibernateConfiguration.getFactory().openSession()) {
            Transaction tx = session.beginTransaction();
            try {
                int rows = session.createQuery("DELETE FROM OrderItem WHERE order.id = :orderId")
                        .setParameter("orderId", order_id)
                        .executeUpdate();
                tx.commit();
                return rows > 0;
            } catch (Exception e) {
                tx.rollback();
                logger.error("Error deleting items for order ID: {}", order_id, e);
                throw new RuntimeException("Failed to delete items", e);
            }
        } catch (Exception e) {
            logger.error("Session error while deleting items for order ID: {}", order_id, e);
            throw new RuntimeException("Session error", e);
        }
    }
}
