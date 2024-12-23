// Class: FoodInventory
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class FoodInventory {
    private Connection connection;
    private Map<String, Integer> foodStock;

    public FoodInventory(Connection connection) {
        this.connection = connection;
        this.foodStock = new HashMap<>();
        loadFoodFromDatabase();
    }

    private void loadFoodFromDatabase() {
        String query = "SELECT food_type, quantity FROM food_inventory";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                foodStock.put(rs.getString("food_type"), rs.getInt("quantity"));
            }
        } catch (SQLException e) {
            System.out.println("Error loading food inventory: " + e.getMessage());
        }
    }

    public void addFood(String foodType, int quantity) {
        foodStock.put(foodType, foodStock.getOrDefault(foodType, 0) + quantity);
        String query = "INSERT INTO food_inventory (food_type, quantity) VALUES (?, ?) ON CONFLICT (food_type) DO UPDATE SET quantity = food_inventory.quantity + EXCLUDED.quantity";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, foodType);
            ps.setInt(2, quantity);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating food inventory: " + e.getMessage());
        }
    }

    public boolean consumeFood(String foodType, int quantity) {
        int currentStock = foodStock.getOrDefault(foodType, 0);
        if (currentStock < quantity) {
            return false; // Tidak cukup stok
        }
        //bila jumlah stock lebih banyak dari jumlah pakan yang akan diberi, stock akan dikurangi 
        foodStock.put(foodType, currentStock - quantity);

        String query = "UPDATE food_inventory SET quantity = ? WHERE food_type = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setInt(1, currentStock - quantity);
            ps.setString(2, foodType);
            ps.executeUpdate();
        } catch (SQLException e) {
            System.out.println("Error updating food inventory: " + e.getMessage());
        }

        return true;
    }

    public void printInventory() {
        System.out.println("Food Inventory:");
        for (Map.Entry<String, Integer> entry : foodStock.entrySet()) {
            System.out.println(entry.getKey() + ": " + entry.getValue());
        }
    }
}