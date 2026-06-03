package org.example;

import org.example.config.DBConnection;
import org.example.config.HibernateConfiguration;
import org.example.dao.impl.OrderDaoImpl;
import org.example.dao.impl.ProductDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.OrderService;
import org.example.ui.MainMenu;
import org.example.ui.ProductsMenu;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

            MainMenu menu = new MainMenu();
            menu.readCommands();

    }
}