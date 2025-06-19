import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBUtil {
    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/login2?useSSL=false&serverTimezone=UTC";
    private static final String DB_USER = "root"; // Update with your database username
    private static final String DB_PASS = ""; // Update with your database password

    static {
        try {
            // Load the MySQL JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC Driver not found. Include it in your library path.");
            e.printStackTrace();
        }
    }

    public static Connection getConnection() {
        Connection connection = null;
        try {
            // Establish the connection
            connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
        } catch (SQLException e) {
            System.err.println("Connection to the database failed.");
            e.printStackTrace();
        }
        return connection;
    }
}
