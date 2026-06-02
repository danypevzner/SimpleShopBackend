package org.example.dao.impl;

import org.example.config.DBConnection;
import org.example.dao.OrderDao;
import org.example.model.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class OrderDaoImpl implements OrderDao {
    private final DBConnection dbConnection;
    private static final String INSERT_SQL = "INSERT INTO shop.orders (user_id, created_at, status, total_price) VALUES ( ?, ?, ?,?)";
    private static final String SELECT_BY_ORDER_ID_SQL = "SELECT order_id, user_id, created_at,status, total_price FROM shop.orders where order_id = ?";
    private static final String SELECT_BY_USER_ID_SQL = "SELECT order_id, user_id, created_at, status, total_price FROM shop.orders where user_id = ?";
    private static final String SELECT_ALL_SQL = "SELECT * FROM shop.orders";
    private static final String UPDATE_STATUS_SQL = "UPDATE shop.orders set status = ? where order_id = ?";
    private static final String DELETE_BY_ORDER_ID_SQL = "DELETE FROM shop.orders where order_id = ?";
    private static final String UPDATE_TOTAL_PRICE_SQL = "UPDATE shop.orders SET total_price = ? where order_id = ?";
    private static Logger logger = LoggerFactory.getLogger(OrderItemDaoImpl.class);
    public OrderDaoImpl() {
        try {
            this.dbConnection = DBConnection.getInstance();
            logger.info("archived DBConnection");

        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }

    @Override
    public int createOrder(Order order) {
        try ( PreparedStatement statement = dbConnection.getConnection().prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){

            //statement.setInt(1,order.getOrder_id());
            statement.setInt(1,order.getUser_id());
            statement.setTimestamp(2, Timestamp.valueOf(order.getCreated_at()));
            statement.setString(3,order.getStatus());
            statement.setBigDecimal(4,order.getTotalPrice());
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
    public Order getOrderByID(int order_id) {
        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(SELECT_BY_ORDER_ID_SQL)){
            statement.setInt(1,order_id);
            ResultSet rs = statement.executeQuery();
            if (rs.next()){
                return new Order(rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getBigDecimal("total_price"));
            }else {
                return null;
            }

        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Order> getAllOrders() {
        List<Order> results = new ArrayList<>();
        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(SELECT_ALL_SQL)){
            ResultSet rs = statement.executeQuery();
            while (rs.next()){
                Order order = new Order(rs.getInt("order_id"),
                        rs.getInt("user_id"),
                        rs.getTimestamp("created_at").toLocalDateTime(),
                        rs.getString("status"),
                        rs.getBigDecimal("total_price"));
                results.add(order);
            }

        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public List<Order> getOrdersByUserId(int user_id) {
        List<Order> results = new ArrayList<>();
        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(SELECT_BY_USER_ID_SQL)){
            statement.setInt(1,user_id);
            try (ResultSet rs = statement.executeQuery();){
                while (rs.next()) {
                    Order order = new Order(rs.getInt("order_id"),
                            rs.getInt("user_id"),
                            rs.getTimestamp("created_at").toLocalDateTime(),
                            rs.getString("status"),
                            rs.getBigDecimal("total_price"));
                    results.add(order);
                }
            }

        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
        return results;
    }

    @Override
    public boolean updateStatus(int order_id, String status) {
        try ( PreparedStatement statement = dbConnection.getConnection().prepareStatement(UPDATE_STATUS_SQL)) {
            statement.setString(1,status);
            statement.setInt(2,order_id);
            int rows =statement.executeUpdate();
            return rows>0;
        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean deleteOrderByID(int order_id) {
        try ( PreparedStatement statement = dbConnection.getConnection().prepareStatement(DELETE_BY_ORDER_ID_SQL)) {
            statement.setInt(1,order_id);
            int res = statement.executeUpdate();
            return res>0;
        }
        catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }

    @Override
    public void updateTotalPrice(int order_id, BigDecimal new_total_price) {
        try (PreparedStatement statement = dbConnection.getConnection().prepareStatement(UPDATE_TOTAL_PRICE_SQL)){
            statement.setBigDecimal(1,new_total_price);
            statement.setInt(2,order_id);
            statement.executeUpdate();
        }catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }

    }
}
