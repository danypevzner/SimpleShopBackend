package org.example.dao.impl;

import org.example.config.DBConnection;
import org.example.config.HibernateConfiguration;
import org.example.model.Product;
import org.example.dao.ProductDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ProductDaoImpl implements ProductDao {

    private static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);
    public ProductDaoImpl() {
        try {

            logger.info("archived DBConnection");
        }
        catch (Exception e){
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }

    }

        @Override
    public int createProduct(Product product) {

        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.save(product);
            tx.commit();
            return product.getProduct_id();

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }


    }

    @Override
    public Product getProductById(int id) {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Product product =session.get(Product.class,id);
            tx.commit();
            return product;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            List<Product> results = session.createQuery("from Product",Product.class).list();
            tx.commit();
            return results;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean alterProduct(Product product) {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.update(product);
            tx.commit();
            return true;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeProduct(int id) {
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.remove(session.get(Product.class,id));
            tx.commit();
            return true;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean updateQuantity(int productId,int quantity){
        try (Session session = HibernateConfiguration.getInstance().getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Product product = session.get(Product.class,productId);
            product.setQuantity(quantity);
            session.update(product);
            tx.commit();
            return true;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }
}
