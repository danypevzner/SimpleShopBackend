package org.example.ui;

import org.example.dao.impl.ProductDaoImpl;
import org.example.model.Product;
import org.example.service.ProductService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Scanner;

public class ProductsMenu {
    ProductService service = new ProductService(new ProductDaoImpl());
    private final Scanner scanner = new Scanner(System.in);
    public void readCommands(){
        while (true){
            System.out.println("\n=== PRODUCT MENU ===");
            System.out.println("1. Add product");
            System.out.println("2. Get product by ID");
            System.out.println("3. Update product");
            System.out.println("4. Show all products");
            System.out.println("5. Delete product by id");
            System.out.println("0. Back");
            System.out.print("Choose option: ");
            switch (scanner.nextLine()){
                case "0" -> {
                    return;
                }
                case "1"->addProduct();
                case "2"->getProductById();
                case "3"->alterProduct();
                case "4"->getAllProducts();
                case "5"->deleteProductByID();

            }
        }
    }
    private void getProductById(){
        try{
            System.out.print("Enter product id: ");
            int id = Integer.parseInt(scanner.nextLine());
            Product product =service.getProductById(id);
            System.out.println("Product found:");
            System.out.println(product.toString());
        }catch (Exception e){
            System.out.println("Error: "+e.getMessage());
        }
    }

    public void deleteProductByID(){
        try {
            System.out.print("Enter product id:");
            int id = Integer.parseInt(scanner.nextLine());
            if (service.removeProduct(id)){
                System.out.println("product deleted");
            };
        }
        catch (Exception e){
            System.out.println("Error:"+e.getMessage());
        }
    }

    private void getAllProducts(){
        try {
            List<Product> products = service.getAllProducts();
            products.stream().forEach(s-> System.out.println(s.toString()));
        } catch (Exception e) {
            System.out.println("Error: "+e.getMessage());
        }
    }

    private void alterProduct(){
        try {
            int id = Integer.parseInt(scanner.nextLine());System.out.print("Enter product id: ");


            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter price: ");
            BigDecimal price = scanner.nextBigDecimal();

            System.out.print("Enter quantity: ");
            Integer quantity = Integer.parseInt(scanner.nextLine());

            Product p = new Product(id, name, price,quantity);
            service.alterProduct(id, name, price,quantity);

            System.out.println("Product changed");
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }

    private void addProduct(){
        try {

            System.out.print("Enter name: ");
            String name = scanner.nextLine();

            System.out.print("Enter price: ");
            BigDecimal price = scanner.nextBigDecimal();
            scanner.nextLine();
            System.out.print("Enter quantity: ");
            Integer quantity = Integer.parseInt(scanner.nextLine());
            //scanner.nextLine();
            System.out.println("Product added with id =");
            System.out.println(service.createProduct(name,price,quantity));
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
