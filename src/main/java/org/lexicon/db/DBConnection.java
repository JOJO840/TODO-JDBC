package org.lexicon.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBConnection {
    private static final String URL = "jdbc:mysql://localhost:3306/todoit";
    private static final String USER = "root";
    private static final String PASSWORD = "1234";

    private static Connection connection;

    public static Connection getConnection() {
        try {
            if (connection == null || connection.isClosed()) {
                connection = DriverManager.getConnection(URL, USER, PASSWORD);
                System.out.println("✅ Database connection established.");
            }
        } catch (SQLException e) {
            System.err.println("❌ Failed to connect to DB: " + e.getMessage());
        }
        return connection;
    }
}
