package org.example.tests;

import org.example.dao.impl.ProductDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.model.Product;
import org.example.service.ProductService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testUtils.TestObjectGenerator;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

public class ProductServiceTest {
    private static final Logger logger = LoggerFactory.getLogger(ProductServiceTest.class);
    private static ProductService service;
    private Product testProduct;
    private int testProductId;

    @BeforeAll
    public static void  setupClass(){
        service = new ProductService(new ProductDaoImpl());
    }

    @BeforeEach
    void setupTest(){
        testProduct = TestObjectGenerator.generateProduct();
        testProductId = service.createProduct(testProduct.getName(),testProduct.getPrice(),testProduct.getQuantity());
    }

    @AfterEach
    void tearDown(){
        if (testProductId!= -1){
            try {
                service.removeProduct(testProductId);
            } catch (Exception e) {
                logger.debug("Failed to remove test user (may already be deleted): {}", e.getMessage());
            }
        }
    }

    @Test
    public void testCreateProduct(){
        assertTrue(testProductId>0, "Product_id>0");

    }

    @Test
    public void testDeleteProduct(){
        assertTrue(service.removeProduct(testProductId));
        assertThrows(IllegalStateException.class,()->service.getProductById(testProductId));
    }

    @Test
    void  testGetProductById(){
        Product found = service.getProductById(testProductId);
        assertEquals(found.getName(),testProduct.getName());
        assertEquals(found.getQuantity(),testProduct.getQuantity());
    }

    @Test
    void testGetAllProducts(){
        Product found = service.getAllProducts().stream().filter(prod1->prod1.getProduct_id()==testProductId).findFirst().get();
        assertEquals(found.getName(),testProduct.getName());
        assertEquals(found.getQuantity(),testProduct.getQuantity());
    }

    @Test
    void getNonExistingProductTest(){
        int incorrect_id  = Integer.MAX_VALUE;
        assertThrows(IllegalStateException.class,()->service.getProductById(incorrect_id));
    }

    @Test
    void removeNonExistingProductTest(){
        int incorrect_id  = Integer.MAX_VALUE;
        assertThrows(IllegalStateException.class,()->service.removeProduct(incorrect_id));
    }



}
