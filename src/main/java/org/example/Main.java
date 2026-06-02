package org.example;

import org.example.config.DBConnection;
import org.example.dao.impl.OrderDaoImpl;
import org.example.dao.impl.ProductDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.User;
import org.example.service.OrderService;
import org.example.ui.MainMenu;
import org.example.ui.ProductsMenu;

import java.math.BigDecimal;
import java.time.LocalDateTime;

//TIP To <b>Run</b> code, press <shortcut actionId="Run"/> or
// click the <icon src="AllIcons.Actions.Execute"/> icon in the gutter.
public class Main {
    public static void main(String[] args) {

            MainMenu menu = new MainMenu();
            menu.readCommands();
        //OrderService service = new OrderService(new OrderDaoImpl());
        //Order o = new Order(1,LocalDateTime.now(),"created",new BigDecimal(111));
        //service.createOrder(o);
    }
}