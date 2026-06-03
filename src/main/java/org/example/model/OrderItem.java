package org.example.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
@Entity @Table(name = "orderItems_HQL")
public class OrderItem {
    @Id
    @GeneratedValue
    private int item_id;
    @ManyToOne
    @JoinColumn(name = "order_id", nullable = false) // Это устанавливает связь с Order
    private Order order;


    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false) // Это устанавливает связь с Order
    private Product product;


    @Column(name = "quantity")
    private int quantity;
    @Column(name = "price")
    private BigDecimal price;


    public OrderItem(Order order, Product product, int quantity, BigDecimal price) {
        this.order = order;
        this.product=product;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem() {
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public Product getProduct() {
        return product;
    }

    public void setProduct_id(Product product) {
        this.product = product;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    @Override
    public String toString(){
        return  item_id+"||"+order+"||"+product.getProduct_id()+"||"+quantity+"||"+price;
    }
}
