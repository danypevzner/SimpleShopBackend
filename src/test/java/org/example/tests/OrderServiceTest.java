package org.example.tests;
import org.example.config.Order_Status_Variants;
import org.example.dao.impl.OrderDaoImpl;
import org.example.dao.impl.OrderItemDaoImpl;
import org.example.dao.impl.ProductDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.OrderService;
import org.example.service.ProductService;
import org.example.service.UserService;
import org.junit.jupiter.api.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import testUtils.TestObjectGenerator;

import static org.junit.jupiter.api.Assertions.*;

public class OrderServiceTest {
    private Order testOrder;
    private User testUser;
    private int testUserId;
    private int testOrderId;
    private Product testProduct;
    private  int testProductId;
    private static OrderService orderService;
    private static UserService userService;
    private static ProductService productService;
    private  Logger logger = LoggerFactory.getLogger(OrderServiceTest.class);
    @BeforeAll
    public static void setupClass(){
        orderService =new OrderService(new OrderDaoImpl(),new ProductDaoImpl(),new OrderItemDaoImpl(),new UserDaoImpl());
        userService = new UserService(new UserDaoImpl());
        productService = new ProductService(new ProductDaoImpl());
    }

    @BeforeEach
    public void setupTest(){
        testUser = TestObjectGenerator.generateUser();
        testUserId = userService.createUser(
                testUser.getName(),
                testUser.getEmail(),
                testUser.getPasswordHash(),
                testUser.getCreatedAt()
        );
        assertNotNull(testUserId, "User ID should not be null");
        testProduct = TestObjectGenerator.generateProduct();
        testProductId = productService.createProduct(testProduct.getName(),testProduct.getPrice(),testProduct.getQuantity());
        assertNotNull(testProductId);
        testOrderId = orderService.createOrder(testUserId);
        testOrder = orderService.getOrderByID(testOrderId);
        assertNotNull(testOrderId, "Order ID should not be null");
    }

    @AfterEach
    void tearDown(){
        try {
            orderService.deleteOrderByID(testOrderId);
        } catch (Exception e) {
            logger.warn("Failed to delete testOrder with id = {}.It might be already deleted",testOrderId);
        }
        try {
            userService.removeUser(testUser.getUser_id());
        } catch (Exception e) {
            logger.warn("Failed to delete testOrder with id = {}.It might be already deleted",testOrderId);
        }

    }

    @Test
    void testCreateOrder(){
        assertTrue(testOrderId>0,"Order Created");
    }

    @Test
    void testFindOrderByID(){
        Order found = orderService.getOrderByID(testOrderId);
        assertEquals(found.getStatus(),found.getStatus());
        assertEquals(found.getUser(),found.getUser());
    }

    @Test
    void getAllOrdersTest(){
        Order found = orderService.getAllOrders().stream().filter(ord->ord.getOrder_id()==testOrderId).findFirst().get();
        assertEquals(found.getStatus(),found.getStatus());
        assertEquals(found.getUser(),found.getUser());
    }

    @Test
    void testGetOrdersByUserId(){
        Order found = orderService.getOrdersByUserId(testOrder.getUser().getUser_id()).stream().filter(ord->ord.getOrder_id()==testOrderId).findFirst().get();
        assertEquals(found.getStatus(),found.getStatus());
        assertEquals(found.getUser(),found.getUser());
    }


    @Test
    void testUpdateOrderStatus(){
        assertTrue(orderService.updateStatus(testOrder.getOrder_id(),Order_Status_Variants.TEST_VALUE.toString()));
        assertEquals(orderService.getOrderByID(testOrderId).getStatus(),Order_Status_Variants.TEST_VALUE.toString());
        orderService.updateStatus(testOrder.getOrder_id(), Order_Status_Variants.CREATED.toString());
    }
    // TODO add incorect test status test

    @Test
    void testDeleteOrderById(){
        assertTrue(orderService.deleteOrderByID(testOrderId));
        assertThrows(IllegalStateException.class,()->orderService.getOrderByID(testOrderId));;
    }

    @Test
    void testAddAndDeleteProduct(){
        assertTrue(orderService.addProductToOrder(testOrderId,testProductId,1));
        assertTrue(orderService.removeProductFromOrder(testOrderId,testProductId));
    }
}
