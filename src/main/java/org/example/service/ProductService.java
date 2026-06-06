package org.example.service;


import org.example.model.Product;
import org.example.dao.ProductDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class ProductService {
    private static Logger logger = LoggerFactory.getLogger(ProductService.class);
    public final ProductDao productDao;

    public ProductService(ProductDao dao) {
        this.productDao = dao;
            logger.info("ProductService initialized");
    }

    public int createProduct(String name,BigDecimal price,int quantity){
        Product product = new Product(name, price,quantity);
        if (product.getPrice().compareTo(BigDecimal.ZERO)==-1){
            logger.warn("Cannot create product with price<0");
            throw new IllegalArgumentException("product_price<0");
        }
        if ((product.getName().length()>45)||(product.getName().length()==0)){
            logger.warn("Cannot create product - incorrect name length");
            throw new IllegalArgumentException("product_name_len incorrect");
        }
        int productId = productDao.createProduct(product);
        logger.info("Product created with id = "+productId);
        return productId;
    }

    public Product getProductById(int id){
        if(id<0){
            logger.warn("Cannot create product - id<0");
            throw new IllegalArgumentException("product_id<0");
        }
        Product product = productDao.getProductById(id);
        ensureExisting(product);
        logger.info("Product with id="+id+" found");
        return  product;
    }

    public boolean alterProduct(int product_id,String name,BigDecimal price,int quantity){
        Product product = new Product(name,price,quantity);
        if (product.getProduct_id()<0){
            logger.warn("Failed to modify product - product_id<0");
            throw new IllegalArgumentException("product_id<0");
        }
        if (product.getPrice().compareTo(BigDecimal.ZERO)==-1){
            logger.warn("Failed to modify product - product_price<0");
            throw new IllegalArgumentException("product_price<0");
        }
        if ((product.getName().length()>45)||(product.getName().length()==0)){
            logger.warn("Failed to modify product - has incorrect name length");
            throw new IllegalArgumentException("product has incorrect name length");
        }
        Product existing = productDao.getProductById(product_id);
        ensureExisting(existing);
        logger.info("Product with id = "+product_id+"altered");
        return productDao.alterProduct(product);
    }

    public boolean removeProduct(int product_id){
        if (product_id<0){
            throw new IllegalArgumentException("product_id<0");
        }
        ensureExisting(getProductById(product_id));
        boolean deleted = productDao.removeProduct(product_id);
        if (!deleted){
            logger.error("Failed to delete product - product not found");
            throw new IllegalStateException("product_not_found");
        };
        logger.info("Product with id = "+product_id+" removed");
        return deleted;
    }

    public List<Product> getAllProducts() {
        logger.info("Product list shown");
        return productDao.getAllProducts();
    }

    private void ensureExisting(Product product){
        if (product==null){
            logger.warn("product not found");
            throw new IllegalStateException("product not found");
        }
    }


}
