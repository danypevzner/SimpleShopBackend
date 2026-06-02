package org.example.service;

import org.example.config.Order_Status_Variants;
import org.example.dao.OrderDao;
import org.example.dao.OrderItemDao;
import org.example.dao.ProductDao;

import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;



public class OrderService {
    public final OrderDao orderDao;
    public final ProductDao productDao;
    public final OrderItemDao orderItemDao;
    private final Logger logger = LoggerFactory.getLogger(OrderService.class);

    public OrderService(OrderDao orderDao, ProductDao productDao, OrderItemDao orderItemDao) {
        this.orderDao = orderDao;
        this.productDao = productDao;
        this.orderItemDao = orderItemDao;
    }

    public int createOrder(int user_id) {
        LocalDateTime creation_time = LocalDateTime.now();
        String status = Order_Status_Variants.CREATED.toString();
        BigDecimal totalPrice = BigDecimal.ZERO;
        Order order = new Order(user_id, creation_time, status, totalPrice);
        if (order.getTotalPrice().compareTo(BigDecimal.ZERO) == -1) {
            logger.warn("Failed to create order - totalPrice < 0");
            throw new RuntimeException("totalPrice is less than 0");
        }
        try {
            Order_Status_Variants.valueOf(order.getStatus());
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to create order- prohibited order status");
            throw new IllegalArgumentException("Prohibited order status value");
        }
        int order_id = orderDao.createOrder(order);
        logger.info("Order created with id = "+order_id);
        return order_id;
    }


    public Order getOrderByID(int order_id) {
        if (order_id < 0) {
            logger.warn("Failed to find order - orderId<0");
            throw new IllegalArgumentException("order_id is less than 0");
        }
        Order order = orderDao.getOrderByID(order_id);
        ensureExists(order);
        logger.info("Order with id ="+order_id+" found");
        return order;
    }


    public List<Order> getAllOrders() {
        List<Order > results = orderDao.getAllOrders();
        logger.info("All orders found with "+results.size()+"results");
        return results;
    }


    public List<Order> getOrdersByUserId(int user_id) {
        if (user_id < 0) {
            logger.warn("Failed to get Orders by id - user_id <0");
            throw new IllegalArgumentException("user_id is less than 0");
        }
        logger.info("orders with user_id ="+user_id+" found");
        return orderDao.getOrdersByUserId(user_id);
    }


    public boolean updateStatus(int order_id, String status) {
        if (order_id < 0) {
            logger.warn("Failed to update order status - order_id < 0");
            throw new IllegalArgumentException("order_id is less than 0");
        }
        try {
            Order_Status_Variants.valueOf(status);
        } catch (IllegalArgumentException e) {
            logger.warn("Failed to update order status - prohibited status value");
            throw new IllegalArgumentException("Prohibited order status value");
        }

        ensureExists(orderDao.getOrderByID(order_id));
        logger.info("Order with id = "+order_id+ " status changed to "+status);


        return orderDao.updateStatus(order_id, status);
    }


    public boolean deleteOrderByID(int order_id) {
        if (order_id < 0) {
            logger.warn("Failed to delete order - order_id < 0");
            throw new IllegalArgumentException("order_id is less than 0");
        }
        Order order = orderDao.getOrderByID(order_id);
        ensureExists(order);
        logger.info("Order with id = "+order_id+" deleted");
        return orderDao.deleteOrderByID(order_id);
    }

    public boolean finalizeOrder(int order_id) {
        if (order_id < 0) {
            logger.warn("Failed to finalize order - order_id < 0");
            throw new IllegalArgumentException("order_id is less than 0");
        }
        Order order = orderDao.getOrderByID(order_id);
        ensureExists(order);
        ensureCanBePaid(order);
        return updateStatus(order_id, String.valueOf(Order_Status_Variants.PAID));

    }

    public boolean cancelOrder(int order_id) {
        if (order_id < 0) {
            logger.warn("Failed to cancel order - order_id < 0");
            throw new IllegalArgumentException("order_id is less than 0");
        }
        Order order = orderDao.getOrderByID(order_id);
        ensureExists(order);
        ensureCanBeCancelled(order);
        return updateStatus(order_id, String.valueOf(Order_Status_Variants.CANCELLED));
    }


    public void recalcTotalPrice(int order_id) {
        if (order_id < 0) {
            logger.error("Failed to recalc total price - order_id < 0");
            throw new RuntimeException("Order_id < 0");
        }
        List<OrderItem> items = orderItemDao.getItemsByOrder(order_id);
        BigDecimal total = items.stream()
                .map(i -> i.getPrice().multiply(BigDecimal.valueOf(i.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        logger.info("Total price of order with id = "+order_id+" updated");
        orderDao.updateTotalPrice(order_id, total);
    }

    public void addProductToOrder(int orderId, int productId, int quantity) {
        Product product = productDao.getProductById(productId);
        Order order = orderDao.getOrderByID(orderId);
        ensureExists(order);
        ensureModifiable(order);
        if (product.getQuantity() < quantity) {
            logger.warn("Failed to add product to Order - not enough stock");
            throw new RuntimeException("Not enough stock");
        }

        // 1. Создаём OrderItem
        OrderItem item = new OrderItem(orderId, productId, quantity, product.getPrice());
        orderItemDao.addItem(item);

        // 2. Уменьшаем количество товара
        int newQuantity = product.getQuantity() - quantity;
        productDao.updateQuantity(productId, newQuantity);
        logger.info("Product with product_id = "+productId+" added to order with id = "+orderId);
        // 3. Пересчитываем totalPrice
        recalcTotalPrice(orderId);
    }

    public void removeProductFromOrder(int orderId, int itemId) {
        Order order = orderDao.getOrderByID(orderId);
        ensureExists(order);
        ensureModifiable(order);
        OrderItem item = orderItemDao.getItemsByOrder(orderId).stream()
                .filter(i -> i.getProduct_id() == itemId)
                .findFirst().orElseThrow(() -> new RuntimeException("item not found"));
        orderItemDao.removeItem(item.getItem_id());
        Product product = productDao.getProductById(item.getProduct_id());
        int newQuantity = product.getQuantity() + item.getQuantity();
        productDao.updateQuantity(product.getProduct_id(), newQuantity);
        logger.info("Product with id ="+product.getProduct_id()+" removed from order with id ="+orderId);
        recalcTotalPrice(orderId);
    }

    private void ensureExists(Order order) {
        if (order == null) {
            logger.warn("Order not found");
            throw new RuntimeException("Order not exists");
        }
    }

    private void ensureModifiable(Order order) {
        if (!order.getStatus().equals(Order_Status_Variants.CREATED.name())) {
            logger.warn("Order with id ="+order.getOrder_id()+" is not modifiable");
            throw new RuntimeException("Order cant be modified-incorrect status:" + order.getStatus());
        }
    }

    private void ensureCanBePaid(Order order) {
        if (!order.getStatus().equals(Order_Status_Variants.CREATED.name())) {
            logger.warn("Order with id ="+order.getOrder_id()+" cannot be paid");
            throw new RuntimeException("Order cant be paid-incorrect status:" + order.getStatus());
        }
    }

    private void ensureCanBeCancelled(Order order) {
        if (order.getStatus().equals(Order_Status_Variants.CANCELLED.name())) {
            logger.warn("Order with id ="+order.getOrder_id()+" is already cancelled");
            throw new RuntimeException("Order is already cancelled");
        }
    }
}
