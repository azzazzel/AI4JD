package com.milendyankov.demos.ai4jd;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class PostgressDB {

    public static String HOST = "localhost";
    public static Integer PORT = 5432;
    public static String DB = "postgres";
    public static String USER = "postgres";
    public static String PASSWORD = "123";



    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(
                "jdbc:postgresql://" + HOST + ":" + PORT + "/" + DB,
                USER,
                PASSWORD);
    }
}
