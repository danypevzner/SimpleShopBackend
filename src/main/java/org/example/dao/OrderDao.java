package org.example.dao;

import org.example.model.Order;

import java.math.BigDecimal;
import java.util.List;

public interface OrderDao {
    public int createOrder(Order order);
    public Order getOrderByID(int order_id);
    public List<Order> getAllOrders();
    public List<Order> getOrdersByUserId(int user_id);
    public boolean updateStatus(int order_id,String status);
    public boolean deleteOrderByID(int order_id);
    public void updateTotalPrice(int order_id, BigDecimal new_total_price);
}
