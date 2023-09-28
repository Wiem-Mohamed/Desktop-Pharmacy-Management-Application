package com.example.hotelmanagmet;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionDb {


    public Connection connecter() {
        Connection con = null;
        String driverName = "com.mysql.cj.jdbc.Driver";

        try {
            Class.forName(driverName);
            System.out.println("Driver loaded");
        } catch (ClassNotFoundException e) {
            System.out.println("Failed to load the driver");
            e.printStackTrace();
        }

        try {
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/mydatabase", "root", "");
            System.out.println("Connected to the database");
        } catch (SQLException e) {
            System.out.println("Failed to connect to the database");
            e.printStackTrace();
        }

        return con;
    }



}