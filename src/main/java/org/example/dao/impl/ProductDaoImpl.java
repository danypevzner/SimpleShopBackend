package org.example.dao.impl;

import org.example.config.DBConnection;
import org.example.model.Product;
import org.example.dao.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {
    private final DBConnection dbConnection;
    private static final String INSERT_SQL = "INSERT INTO shop.products (name, price, quantity) VALUES ( ?, ?, ?)";
    private static final String SELECT_BY_ID_SQL = "select product_id, name, price, quantity from shop.products where product_id = ?";
    private static final String SELECT_ALL_SQL = "select product_id, name, price, quantity from shop.products";
    private static final String UPDATE_SQL = "UPDATE shop.products set name = ?, price = ?,quantity = ? where product_id = ?";
    private static final String DELETE_BY_ID_SQL = "delete from shop.products where product_id = ?";
    private static final String UPDATE_QUANTITY_SQL = "UPDATE shop.products SET quantity = ? WHERE product_id = ?";
    private static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
    public ProductDaoImpl() {
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
    public int createProduct(Product product) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(INSERT_SQL, Statement.RETURN_GENERATED_KEYS)){
            statement.setString(1,product.getName());
            statement.setBigDecimal(2,product.getPrice());
            statement.setInt(3,product.getQuantity());
            statement.executeUpdate();
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (keys.next()) {
                    return keys.getInt(1);
                }
            }
            logger.error("Failed to retrieve generated product_id");
            throw new RuntimeException("Failed to retrieve generated product_id");

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }


    }

    @Override
    public Product getProductById(int id) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_BY_ID_SQL)) {
            statement.setInt(1, id);
            try (ResultSet rs = statement.executeQuery()) {

                if (rs.next()) {
                    Product product = new Product();
                    product.setProduct_id(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setQuantity(rs.getInt("quantity"));
                    return product;
                } else {
                    return null;
                }
            }

        } catch (SQLException e) {
            // логирование + проброс
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

    @Override
    public List<Product> getAllProducts() {
        Connection connection = dbConnection.getConnection();
        List<Product> results = new ArrayList<>();
        try (PreparedStatement statement = connection.prepareStatement(SELECT_ALL_SQL)) {
            try (ResultSet rs = statement.executeQuery()) {

                while (rs.next()) {
                    Product product = new Product();
                    product.setProduct_id(rs.getInt("product_id"));
                    product.setName(rs.getString("name"));
                    product.setPrice(rs.getBigDecimal("price"));
                    product.setQuantity(rs.getInt("quantity"));
                    results.add(product);
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
    public boolean alterProduct(Product product) {
        Connection connection = dbConnection.getConnection();
        try {
            PreparedStatement statement = connection.prepareStatement(UPDATE_SQL);

            statement.setString(1,product.getName());
            statement.setBigDecimal(2,product.getPrice());
            statement.setInt(3,product.getQuantity());
            statement.setInt(4,product.getProduct_id());
            int rows = statement.executeUpdate();
            return rows>0;

        } catch (SQLException e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeProduct(int id) {
        Connection connection = dbConnection.getConnection();
        try (PreparedStatement statement = connection.prepareStatement(DELETE_BY_ID_SQL)) {
            statement.setInt(1, id);
            int rows = statement.executeUpdate();
            return rows>0;
        } catch (SQLException e) {
            // логирование + проброс
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean updateQuantity(int productId,int quantity){
        Connection connection = dbConnection.getConnection();
        try(PreparedStatement statement = connection.prepareStatement(UPDATE_QUANTITY_SQL)) {
            statement.setInt(1,quantity);
            statement.setInt(2,productId);
            int rows = statement.executeUpdate();
            return rows>0;
        }
        catch (SQLException e){
            logger.error(e.getMessage());
            throw new RuntimeException(e.getMessage());
        }
    }
}
