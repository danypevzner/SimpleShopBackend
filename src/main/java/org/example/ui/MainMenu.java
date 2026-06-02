package org.example.ui;

import java.util.Scanner;

public class MainMenu {
    public final UserMenu userMenu;
    public final ProductsMenu productsMenu;
    public final OrderMenu orderMenu;
    Scanner scanner = new Scanner(System.in);
    public MainMenu() {
        this.userMenu = new UserMenu();
        this.productsMenu = new ProductsMenu();
        this.orderMenu = new OrderMenu();
    }

    public void readCommands(){
        while (true) {
            System.out.println("=== MAIN_MENU ===");
            System.out.println("1:Products");
            System.out.println("2:User");
            System.out.println("3:Orders");
            System.out.println("0:Exit");
            System.out.print("Choose an option:");
            switch (scanner.nextLine()) {
                case "1" -> productsMenu.readCommands();
                case "2" -> userMenu.readCommands();
                case "3" -> orderMenu.readCommands();
                case "0" -> {
                    return;
                }
                default -> System.out.println("Unknown option");
            }
        }
    }

}
