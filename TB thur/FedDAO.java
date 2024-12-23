// Class: FedDAO
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class FedDAO {
    private Connection connection;

    public FedDAO(Connection connection) {
        this.connection = connection;
    }
    //kodingan untuk menginputkan data ke database fed
    public void addFedRecord(String animalId, String feederId, String foodType) throws SQLException {
        String query = "INSERT INTO fed (animal_id, feeder_id, food_type) VALUES (?, ?, ?)";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, animalId);
            ps.setString(2, feederId);
            ps.setString(3, foodType);
            ps.executeUpdate();
            System.out.println("Fed record added successfully.");
        }
    }
//kodingan untuk menampilkan isi databasenya
    public List<String[]> getFedRecords(String orderBy) throws SQLException {
        List<String[]> records = new ArrayList<>();
        String query = "SELECT * FROM fed ORDER BY " + orderBy;
        try (PreparedStatement ps = connection.prepareStatement(query);
        ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                records.add(new String[]{
                    rs.getString("id"),
                    rs.getString("animal_id"),
                    rs.getString("feeder_id"),
                    rs.getString("food_type"),
                    rs.getString("fed_time")
                });
            }
        }
        return records;
    }
//kodingan untuk menghapus isi databse fed
    public void deleteFedRecord(int recordId) throws SQLException {
        String query = "DELETE FROM fed WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, recordId);
            ps.executeUpdate();
            System.out.println("Fed record deleted successfully.");
        }
    }

    public void staffName(String feederId) throws SQLException {
        
        
    }
}
