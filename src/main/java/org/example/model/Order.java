package org.example.model;

import jakarta.persistence.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "orders_HQL")
public class Order {
    @Id
    @GeneratedValue
    private int order_id;
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<OrderItem> items;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false) // Это устанавливает связь с Order
    private User user;

    @Column(name = "createdAt")
    private LocalDateTime created_at;
    @Column(name = "status")
    private String status;
    @Column(name = "totalPrice")
    private BigDecimal totalPrice;


    public Order( User user, LocalDateTime created_at, String status, BigDecimal totalPrice) {
        this.user = user;
        this.created_at = created_at;
        this.status = status;
        this.totalPrice = totalPrice;
    }

    public Order() {
    }


    public int getOrder_id() {
        return order_id;
    }

    public void setOrder_id(int order_id) {
        this.order_id = order_id;
    }

    public User getUser() {
        return user;
    }

    public void setUser_id(User user) {
        this.user = user;
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
        return order_id+"||"+user.getUser_id()+"||"+created_at+"||"+status+"||"+totalPrice;
    }
}
