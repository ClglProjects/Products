package Warenwirtschaft.products.user;

import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static String DB_URL;
    private static String DB_USER;
    private static String DB_PASSWORD;

    static {
        loadConfiguration();
    }

    /**
     * Lädt die Datenbankkonfiguration aus der Datei 'config.properties'.
     */
    private static void loadConfiguration() {
        Properties props = new Properties();
        try (FileInputStream fis = new FileInputStream("src/main/resources/config.properties")) {
            props.load(fis);
            DB_URL = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASSWORD = props.getProperty("db.password");
        } catch (IOException e) {
            System.err.println("❌ Fehler beim Laden der Konfigurationsdatei:");
            e.printStackTrace();
            throw new RuntimeException("Datenbank-Konfiguration konnte nicht geladen werden.");
        }
    }

    /**
     * Stellt eine Verbindung zur Datenbank her.
     * @return Eine aktive Datenbankverbindung.
     */
    public static Connection getConnection() {
        try {
            return DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
        } catch (SQLException e) {
            System.err.println("❌ Fehler bei der Verbindung zur Datenbank:");
            e.printStackTrace();
            throw new RuntimeException("Verbindung zur Datenbank fehlgeschlagen.");
        }
    }
}
