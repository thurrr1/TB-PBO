// Class: DatabaseConnection
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

//kodingan untuk hubungan ke database nya
public class DatabaseConnection {
    private static final String URL = "jdbc:postgresql://localhost:5432/Zoo";
    private static final String USER = "postgres";
    private static final String PASSWORD = "Kamil12";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL, USER, PASSWORD);
    }
}
