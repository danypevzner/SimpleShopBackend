package org.example.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private Connection connection;
    private static final String DB_URL = "jdbc:mysql://localhost:3306/def";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "root";
    private static DBConnection instance;

    private DBConnection() throws SQLException {
        this.connection = DriverManager.getConnection(DB_URL,DB_USER,DB_PASSWORD);
    };

    public static DBConnection getInstance() throws SQLException {
        if (instance==null){
            instance = new DBConnection();
        }
        return instance;
    }

    public Connection getConnection(){
        return  this.connection;
    }
}
