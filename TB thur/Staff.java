import java.sql.Connection;

public class Staff {
    protected Connection connection; // Aksesible oleh subclass

    public Staff(Connection connection) {
        this.connection = connection; // Simpan connection di atribut
    }
}
