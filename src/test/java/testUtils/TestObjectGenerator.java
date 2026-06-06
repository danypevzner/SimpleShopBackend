package testUtils;

import org.example.config.Order_Status_Variants;
import org.example.model.Order;
import org.example.model.Product;
import org.example.model.User;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class TestObjectGenerator {

    public static User generateUser(){
        Double test_user_number = Double.valueOf(String.valueOf(Math.random()*10000/10000));
        String name = "testuser"+test_user_number;
        String email = "testuser"+test_user_number+"@testmail.ru";
        String passwordHash = test_user_number+"1111111111";
        return new User(name,email,passwordHash, LocalDateTime.now());
    }

    public static Product generateProduct(){
        String ind = String.valueOf(Math.random()*10000/10000);
        String name = "testproduct#"+ind;
        BigDecimal price = BigDecimal.valueOf(Math.random()*1000);
        int quantity = 33;
        return new Product(name,price,quantity);
    }


    public static Order generateOrder(){
        String ind = String.valueOf(Math.random()*10000/10000);
        return new Order(generateUser(),LocalDateTime.now(), Order_Status_Variants.CREATED.toString(),BigDecimal.ZERO);
    }

    private TestObjectGenerator(){}


}
