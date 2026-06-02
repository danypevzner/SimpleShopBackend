package org.example.model;

import java.math.BigDecimal;

public class OrderItem {
    private int item_id;
    private int order_id;
    private int product_id;
    private int quantity;
    private BigDecimal price;

    public OrderItem(int item_id, int order_id, int product_id, int quantity, BigDecimal price) {
        this.item_id = item_id;
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
    }

    public OrderItem(int order_id, int product_id, int quantity, BigDecimal price) {
        this.order_id = order_id;
        this.product_id = product_id;
        this.quantity = quantity;
        this.price = price;
    }

    public int getItem_id() {
        return item_id;
    }

    public void setItem_id(int item_id) {
        this.item_id = item_id;
    }

    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public void setProduct_id(int product_id) {
        this.product_id = product_id;
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
        return  item_id+"||"+order_id+"||"+product_id+"||"+quantity+"||"+price;
    }
}
