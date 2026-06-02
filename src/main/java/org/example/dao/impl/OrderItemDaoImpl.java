package org.example.dao.impl;

import org.example.config.DBConnection;
import org.example.dao.OrderItemDao;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderItemDaoImpl implements OrderItemDao {
    private final DBConnection dbConnection;
    private final String insert_SQL = "INSERT INTO shop.order_items (order_id,product_id,quantity,price) VALUES (?,?,?,?)";
    private final String delete_SQL = "DELETE FROM shop.order_items WHERE item_id = ?";
    private final String get_items_by_order_SQL = "SELECT item_id,product_id,quantity,price from shop.order_items where order_id = ? ORDER BY item_id";
    private final String delete_items_by_order_SQL = "DELETE FROM shop.order_items WHERE order_id = ?";
    private static  Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);
    public OrderItemDaoImpl() {
        try {
            this.dbConnection = DBConnection.getInstance();
            logger.info("archived DBConnection");
        }
        catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public int addItem(OrderItem item) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(insert_SQL, Statement.RETURN_GENERATED_KEYS)){
            statement.setInt(1,item.getOrder_id());
            statement.setInt(2,item.getProduct_id());
            statement.setInt(3,item.getQuantity());
            statement.setBigDecimal(4,item.getPrice());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            logger.error("Failed to retrieve generated orderItem_id");
            throw new RuntimeException("Failed to retrieve generated orderItem_id");
        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public boolean removeItem(int item_id) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(delete_SQL)){
            statement.setInt(1,item_id);
            int rows = statement.executeUpdate();
            return rows>0;
        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public List<OrderItem> getItemsByOrder(int order_id) {
        Connection connection = dbConnection.getConnection();
        List<OrderItem> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(get_items_by_order_SQL)) {
            statement.setInt(1,order_id);
            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    int item_id = rs.getInt("item_id");
                    int product_id = rs.getInt("product_id");
                    int quantity = rs.getInt("quantity");
                    BigDecimal price = rs.getBigDecimal("price");
                    OrderItem item = new OrderItem(item_id,order_id,product_id,quantity,price);
                    results.add(item);
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
    public boolean deleteItemsByOrder(int order_id) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(delete_items_by_order_SQL)){
            statement.setInt(1,order_id);
            int rows = statement.executeUpdate();
            return rows>0;
        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
