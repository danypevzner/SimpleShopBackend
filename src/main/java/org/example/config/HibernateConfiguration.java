package org.example.config;


import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.example.model.User;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class HibernateConfiguration {
    private final SessionFactory factory;
    private static HibernateConfiguration instance;
    private HibernateConfiguration() {
        Configuration configuration = new Configuration();
        configuration.setProperty("hibernate.connection.driver_class", "com.mysql.cj.jdbc.Driver");
        configuration.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/def");
        configuration.setProperty("hibernate.connection.username", "root");
        configuration.setProperty("hibernate.connection.password", "root");
        configuration.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL8Dialect");
        configuration.setProperty("hibernate.hbm2ddl.auto", "update");
        configuration.addAnnotatedClass(Order.class);
        configuration.addAnnotatedClass(OrderItem.class);
        configuration.addAnnotatedClass(Product.class);
        configuration.addAnnotatedClass(User.class);
        factory = configuration.buildSessionFactory();
    }

    public static HibernateConfiguration getInstance(){
        if (instance == null){
           instance = new HibernateConfiguration();
        }
        return instance;
    }

     public SessionFactory getFactory(){
        return this.factory;
    }
}
