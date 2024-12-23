import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Feeder extends Staff implements ZooStaff {
    private String staffname = "Unknown Staff";
    private String animalname = "Unknown Animal";

    // Constructor yang memanggil superclass
    public Feeder(Connection connection) {
        super(connection); // Panggil constructor dari Staff
    }

    // Implementasi dari ZooStaff
    @Override
    public void performTask() {
        System.out.print(" is feeding the ");
    }

    // Method untuk memberi makan hewan
    public void feedAnimal(String animalId, String feederId) {
        // Query untuk mendapatkan data staff
        String query = "SELECT * FROM staff WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setString(1, feederId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) { // Jika data ditemukan
                staffname = rs.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Query untuk mendapatkan data animal
        String query2 = "SELECT * FROM animals WHERE id = ?";
        try (PreparedStatement stm = connection.prepareStatement(query2)) {
            stm.setString(1, animalId);
            ResultSet rsj = stm.executeQuery();
            if (rsj.next()) { // Jika data ditemukan
                animalname = rsj.getString("name");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Output ke console
        System.out.print(staffname);
        performTask();
        System.out.println(animalname);
    }
}
