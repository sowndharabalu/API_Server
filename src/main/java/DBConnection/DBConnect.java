package DBConnection;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

class DBConfig{
    public static final String URL = "jdbc:mysql://localhost:3306/api_db";
    public static final String USER = "root";
    public static final String PASSWORD = "welcome";
}

public class DBConnect {
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(DBConfig.URL, DBConfig.USER, DBConfig.PASSWORD);
    }
}
