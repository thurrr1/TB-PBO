import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class FoodSupplier extends Staff implements ZooStaff {
    private String name = "Unknown Supplier";

    // Constructor yang memanggil superclass
    public FoodSupplier(Connection connection) {
        super(connection); // Panggil constructor dari Staff
    }

    // Implementasi dari ZooStaff
    @Override
    public void performTask() {
        System.out.println(name + " is restocking the food inventory.");
    }

    // Method untuk suplai makanan
    public void supply(String idsupplier) {
        String query = "SELECT * FROM staff WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, idsupplier);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Jika data ditemukan
                name = rs.getString("name");
            } else {
                System.out.println("Supplier with ID " + idsupplier + " not found in the database.");
                return; // Keluar jika tidak ada hasil
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Jalankan tugas setelah nama ditemukan
        performTask();
    }
}
