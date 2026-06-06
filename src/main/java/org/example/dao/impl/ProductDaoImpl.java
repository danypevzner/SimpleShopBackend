package org.example.dao.impl;

import org.example.config.HibernateConfiguration;
import org.example.model.Product;
import org.example.dao.ProductDao;
import org.hibernate.Session;
import org.hibernate.Transaction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

public class ProductDaoImpl implements ProductDao {

    private static Logger logger = LoggerFactory.getLogger(ProductDaoImpl.class);


        @Override
    public int createProduct(Product product) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            session.persist(product);
            tx.commit();
            return product.getProduct_id();
        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public Product getProductById(int id) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Product product =session.get(Product.class,id);
            if (product == null) {
                logger.warn("Product with id = {} not found",id);
            }
            tx.commit();
            return product;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Product> getAllProducts() {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
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
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Product existing = getProductById(product.getProduct_id());
            if (existing == null) {
                logger.warn("Product with id = {} not found",product.getProduct_id());
                return false;
            }
            session.merge(product);
            tx.commit();
            return true;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean removeProduct(int id) {
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Product product = getProductById(id);

            if (product == null) {
                logger.warn("Product with id = {} not found",product.getProduct_id());
                return false;
            }
            session.remove(product);
            tx.commit();
            return true;

        } catch (Exception e) {
            logger.error(e.getMessage());
            throw new RuntimeException(e);
        }
    }

    public boolean updateQuantity(int productId,int quantity){
        try (Session session = HibernateConfiguration.getFactory().openSession()){
            Transaction tx = session.beginTransaction();
            Product product = session.get(Product.class,productId);
            if (product == null) {
                logger.warn("Product with id = {} not found",product.getProduct_id());
                return false;
            }
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
