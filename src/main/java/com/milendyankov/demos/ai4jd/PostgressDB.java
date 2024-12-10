package com.milendyankov.demos.ai4jd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgressDB {

    public static String HOST = "localhost";
    public static Integer PORT = 5433;
    public static String DB = "postgres";
    public static String USER = "postgres";
    public static String PASSWORD = "123";



    public static Connection getConnection() throws SQLException {
        Connection conn = DriverManager.getConnection(
                "jdbc:postgresql://localhost:5433/postgres",
                "postgres",
                "123");
        return conn;
    }
}
