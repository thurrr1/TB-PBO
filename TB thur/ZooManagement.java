// Class: ZooManagement (Main Class)
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class ZooManagement {
    public static void main(String[] args) {
        try (Connection connection = DatabaseConnection.getConnection()) {
            FedDAO fedDAO = new FedDAO(connection);
            Feeder feeder = new Feeder(connection);
            FoodSupplier supp = new FoodSupplier(connection);
            FoodInventory foodInventory = new FoodInventory(connection);

            @SuppressWarnings("resource")
			Scanner scanner = new Scanner(System.in);
            boolean running = true;
            while (running) {
                // ini menu yang bisa dipilih oleh manager
                System.out.println("\nPilih operasi:");
                System.out.println("1. Tambah catatan pemberian makan");
                System.out.println("2. Tampilkan catatan pemberian makan");
                System.out.println("3. Hapus catatan pemberian makan");
                System.out.println("4. Tambah stok makanan");
                System.out.println("5. Tampilkan daftar makanan di inventaris");
                System.out.println("6. Tampilkan daftar staff");
                System.out.println("7. Tampilkan daftar hewan");
                System.out.println("8. Keluar");
                System.out.print("Pilihan: ");

                int choice = scanner.nextInt();
                scanner.nextLine(); // Clear buffer

                switch (choice) {
                    case 1:
                        System.out.print("Masukkan ID Hewan: ");
                        String animalId = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Masukkan ID Feeder: ");
                        String feederId = scanner.nextLine().trim().toUpperCase();
                        //menggunakan trim dan touppercase agar tidak terjadi kesalahan saat mencari data ke database
                        System.out.print("Masukkan Jenis Makanan: ");
                        String foodType = scanner.nextLine();
                        //memeriksa terlebih dahulu hubungannya dengan data yang ada di database
                        if (!isValidId(connection, "animals", animalId)) {
                            System.out.println("Error: ID Hewan tidak ditemukan.");
                            break;
                        }

                        if (!isValidId(connection, "staff", feederId)) {
                            System.out.println("Error: ID Feeder tidak ditemukan.");
                            break;
                        }

                        if (!foodInventory.consumeFood(foodType, 15)) {
                            System.out.println("Stok makanan tidak cukup untuk memberi makan.");
                            break;
                        }
                        //menyimpan data imputan ke database
                        feeder.feedAnimal(animalId, feederId);
                        fedDAO.addFedRecord(animalId, feederId, foodType);
                        break;
                    case 2:
                    //memberi pilihan urutan saat menampilkan data 
                        System.out.println("Urutkan berdasarkan:");
                        System.out.println("1. Waktu pemberian makan");
                        System.out.println("2. Nama hewan");
                        int sortChoice = scanner.nextInt();
                        scanner.nextLine(); // Clear buffer
                        String orderBy = sortChoice == 1 ? "fed_time DESC" : "animal_id";

                        //ditampilkan berdasarkan urutan yang dipilih
                        List<String[]> records = fedDAO.getFedRecords(orderBy);
                        for (String[] record : records) {
                            System.out.printf("ID: %s, Animal ID: %s, Feeder ID: %s, Food Type: %s, Fed Time: %s%n",
                                    record[0], record[1], record[2], record[3], record[4]);
                        }
                        break;
                    case 3:
                        System.out.print("Masukkan ID catatan yang ingin dihapus: ");
                        int recordId = scanner.nextInt();
                        //menghapus data yang dipilih
                        fedDAO.deleteFedRecord(recordId);
                        break;
                    case 4:
                        System.out.print("Masukkan ID Supplier: ");
                        String idsupplier = scanner.nextLine().trim().toUpperCase();
                        System.out.print("Masukkan Jenis Makanan: ");
                        String newFoodType = scanner.nextLine();
                        System.out.print("Masukkan Jumlah Stok: ");
                        int quantity = scanner.nextInt();
                        //menyimpan data imputan
                        foodInventory.addFood(newFoodType, quantity);
                        supp.supply(idsupplier);
                        System.out.println("Stok makanan berhasil ditambahkan.");
                        
                        break;
                    case 5:
                    //menampilkan daftar makanan dari database food inventory
                        foodInventory.printInventory();
                        break;
                    case 6:
                    //menampilkan id staff dan nama staff dari database
                        displayStaff(connection);
                        break;
                    case 7:
                    //menampilkan id, nama, dan type hewan dari database
                        displayAnimals(connection);
                        break;
                    case 8:
                    //untuk terminate program
                        running = false;
                        System.out.println("Keluar dari program. Terima kasih!");
                        break;
                    default:
                        System.out.println("Pilihan tidak valid. Coba lagi.");
                }
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
    }
//boolean untuk ke-valid-an
    private static boolean isValidId(Connection connection, String tableName, String id) {
        String query = "SELECT 1 FROM " + tableName + " WHERE id = ?";
        try (PreparedStatement ps = connection.prepareStatement(query)) {
            ps.setString(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            System.out.println("Error checking ID in " + tableName + ": " + e.getMessage());
            return false;
        }
    }
//kodingan untuk menampilkan nama staff
    private static void displayStaff(Connection connection) {
        String query = "SELECT id, name, role FROM staff";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Daftar Staff:");
            while (rs.next()) {
                System.out.printf("ID: %s, Name: %s, Role: %s%n",
                        rs.getString("id"), rs.getString("name"), rs.getString("role"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying staff: " + e.getMessage());
        }
    }
//kodingan untuk menampilkan nama hewan
    private static void displayAnimals(Connection connection) {
        String query = "SELECT id, name, diet_type FROM animals";
        try (PreparedStatement ps = connection.prepareStatement(query);
             ResultSet rs = ps.executeQuery()) {
            System.out.println("Daftar Hewan:");
            while (rs.next()) {
                System.out.printf("ID: %s, Name: %s, Diet: %s%n",
                        rs.getString("id"), rs.getString("name"), rs.getString("diet_type"));
            }
        } catch (SQLException e) {
            System.out.println("Error displaying animals: " + e.getMessage());
        }
    }
}
