package org.example.dao;

import org.example.model.OrderItem;

import java.util.List;

public interface OrderItemDao {
    public int addItem(OrderItem item);
    public boolean removeItem(int item_id);
    public List<OrderItem> getItemsByOrder(int order_id);
    public boolean deleteItemsByOrder(int order_id);
}
