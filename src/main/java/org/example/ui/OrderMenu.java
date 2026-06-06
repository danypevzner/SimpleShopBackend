package org.example.ui;

import org.example.config.Order_Status_Variants;
import org.example.dao.impl.OrderDaoImpl;
import org.example.dao.impl.OrderItemDaoImpl;
import org.example.dao.impl.ProductDaoImpl;
import org.example.dao.impl.UserDaoImpl;
import org.example.model.Order;
import org.example.model.OrderItem;
import org.example.model.Product;
import org.example.service.OrderService;
import org.example.service.ProductService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Scanner;

public class OrderMenu {

    public final OrderService service;
    public final ProductService productService;
    public final Scanner scanner;
    public OrderMenu() {
        this.service = new OrderService(new OrderDaoImpl(),new ProductDaoImpl(),new OrderItemDaoImpl(),new UserDaoImpl());
        this.scanner = new Scanner(System.in);
        this.productService = new ProductService(new ProductDaoImpl());
    }

    public void readCommands(){
          while (true){
              System.out.println("=== ORDER MENU ===");
              System.out.println("1. Create new order");
              System.out.println("2. Add product to order");
              System.out.println("3. Remove product from order");
              System.out.println("4. Show order by ID");
              System.out.println("5. Show all orders");
              System.out.println("6. Show orders by user");
              System.out.println("7. Finalize order");
              System.out.println("8. Cancel order");
              System.out.println("9. Delete order");
              System.out.println("0. Back");
              System.out.println("Choose an option:");
              switch (scanner.nextLine()){
                  case "0"-> {
                      return;
                  }
                  case "1"->createOrder();
                  case "2"->addProduct();
                  case "3"->removeProduct();
                  case "4"->showOrderByID();
                  case "5"->showAllOrders();
                  case "6"->showOrdersByUser();
                  case "7"->finalizeOrder();
                  case "8"->cancelOrder();
                  case "9"->deleteOrder();
                  default-> System.out.println("Uncommon option");
              }

          }
    }

    public void createOrder(){
        try {
            System.out.print("enter user_id:");
            int user_id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Order added with id =");
            System.out.println(service.createOrder(user_id));

        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }




    }
    public void addProduct(){
        try {
            System.out.print("Enter order_id: ");
            int orderId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter product_id: ");
            int productId = scanner.nextInt();
            scanner.nextLine();

            System.out.print("Enter quantity: ");
            int quantity = scanner.nextInt();
            scanner.nextLine();

            service.addProductToOrder(orderId, productId, quantity);

            System.out.println("Product added to order.");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }

    }
    public void removeProduct(){
        try {
            System.out.print("Enter order_id:");
            int order_id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("Enter item_id:");
            int item_id = scanner.nextInt();
            scanner.nextLine();
            service.removeProductFromOrder(order_id,item_id);
            //service.recalcTotalPrice(order_id);
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void showOrderByID(){
        try{
            System.out.print("Enter order_Id:");
            int orderId = scanner.nextInt();
            scanner.nextLine();
            System.out.println(service.getOrderByID(orderId).toString());
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void showAllOrders(){
        try{
            service.getAllOrders().stream().forEach(order -> System.out.println(order.toString()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void showOrdersByUser(){
        try{
            System.out.print("Enter user_Id:");
            int userId = scanner.nextInt();
            scanner.nextLine();
            service.getOrdersByUserId(userId).stream().forEach(order -> System.out.println(order.toString()));
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }
    public void finalizeOrder(){
        System.out.print("Enter order_Id:");
        int order_id = scanner.nextInt();
        scanner.nextLine();
        service.finalizeOrder(order_id);

    }
    public void cancelOrder(){
        System.out.print("Enter order_Id:");
        int order_id = scanner.nextInt();
        scanner.nextLine();
        service.cancelOrder(order_id);
    }
    public void deleteOrder(){
        System.out.print("Enter order_Id:");
        int order_id = scanner.nextInt();
        scanner.nextLine();
        service.deleteOrderByID(order_id);
    }

}
