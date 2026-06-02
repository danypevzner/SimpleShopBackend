package org.example.ui;

import org.example.config.DateTimeInput;
import org.example.config.PasswordHasher;
import org.example.dao.impl.UserDaoImpl;
import org.example.model.User;
import org.example.service.UserService;

import java.time.LocalDateTime;
import java.util.Scanner;

public class UserMenu {
    Scanner scanner = new Scanner(System.in);
    UserService userService;

    public UserMenu() {
        this.userService = new UserService(new UserDaoImpl());
    }

    public void readCommands(){

        while (true){
            System.out.println("\n=== USER_MENU ===");
            System.out.println("1:Create new user");
            System.out.println("2:Find user by ID");
            System.out.println("3:Find all users");
            System.out.println("4:Delete user by ID");
            System.out.println("5:Alter user");
            System.out.println("0:Back");
            System.out.print("Choose an option:");
            switch (scanner.nextLine()){
                case "1"->createUser();
                case "2"->findUserByID();
                case "3"->showAllUsers();
                case "4"->deleteUserByID();
                case "5"->alterUser();
                case "0"->{
                    return;
                }
            }
        }

    }
    public void createUser(){
        try {
            System.out.print("enter username:");
            String name = scanner.nextLine();
            System.out.print("enter email:");
            String email = scanner.nextLine();
            System.out.print("Enter password:");
            String passwordHash = PasswordHasher.hashPassword(scanner.nextLine());
            System.out.print("user created with id=");
            System.out.println(userService.createUser(name, email, passwordHash, LocalDateTime.now()));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void findUserByID(){
        try {
            System.out.print("enter_id:");
            int id = scanner.nextInt();
            scanner.nextLine();
            User user = userService.getUserById(id);
            System.out.println(user.toString());
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void showAllUsers(){
        try {
            userService.getAllUsers().stream().forEach(user->System.out.println(user.toString()));
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void deleteUserByID(){
        try {
            System.out.print("enter id:");
            int id = scanner.nextInt();
            scanner.nextLine();
            userService.removeUser(id);
            System.out.println("user removed");
        }catch (Exception e){
            System.out.println(e.getMessage());
        }

    }

    public void alterUser(){
        try {
            int user_id = scanner.nextInt();
            scanner.nextLine();
            System.out.print("enter username:");
            String name = scanner.nextLine();
            System.out.print("enter email:");
            String email = scanner.nextLine();
            System.out.print("Enter password:");
            String passwordHash = PasswordHasher.hashPassword(scanner.nextLine());
            System.out.println("enter creation_date in format dd-MM-yyyy HH:mm");
            System.out.println("enter <<now>> to set current datetime");
            LocalDateTime created_at = DateTimeInput.readLocalDateTime(scanner,"");
            userService.alterUser(user_id,name,email,passwordHash,created_at);
            System.out.println("User chenged");
        }
        catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

}
