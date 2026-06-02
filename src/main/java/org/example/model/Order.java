package org.example.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class Order {
    private int order_id;
    private int user_id;
    private LocalDateTime created_at;
    private String status;
    private BigDecimal totalPrice;

    public Order(Integer order_id, int user_id, LocalDateTime created_at, String status, BigDecimal totalPrice) {
        this.order_id = order_id;
        this.user_id = user_id;
        this.created_at = created_at;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Order( int user_id, LocalDateTime created_at, String status, BigDecimal totalPrice) {
        this.user_id = user_id;
        this.created_at = created_at;
        this.status = status;
        this.totalPrice = totalPrice;
    }


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public LocalDateTime getCreated_at() {
        return created_at;
    }

    public void setCreated_at(LocalDateTime created_at) {
        this.created_at = created_at;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public BigDecimal getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(BigDecimal totalPrice) {
        this.totalPrice = totalPrice;
    }
    @Override
    public String toString(){
        return user_id+"||"+user_id+"||"+created_at+"||"+status+"||"+totalPrice;
    }
}
