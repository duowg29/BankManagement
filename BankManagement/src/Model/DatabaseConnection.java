package Model;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String username = "sa";
    private static final String password = "12345678";
    private static final String url = "jdbc:sqlserver://localhost:1433;databaseName=Banking";

    public static Connection getConnection() throws ClassNotFoundException, SQLException, IOException {
        Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        return DriverManager.getConnection(url, username, password);
    }

   

    public void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
   
}