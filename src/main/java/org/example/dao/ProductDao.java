package org.example.dao;

import org.example.model.Product;

import java.util.List;

public interface ProductDao {
    public int createProduct(Product product);
    public Product getProductById(int id);
    public List<Product> getAllProducts();
    public boolean alterProduct(Product product);
    public boolean removeProduct(int id);
    public boolean updateQuantity(int product_id,int newQuantity);
}
