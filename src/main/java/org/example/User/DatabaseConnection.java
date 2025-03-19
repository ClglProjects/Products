package org.example.User;

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
        try {
            // Lädt die Konfigurationsdatei
            Properties props = new Properties();
            FileInputStream fis = new FileInputStream("src/main/resources/config.properties");
            props.load(fis);

            // Setze die Verbindungsdaten
            DB_URL = props.getProperty("db.url");
            DB_USER = props.getProperty("db.user");
            DB_PASSWORD = props.getProperty("db.password");

        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler beim Laden der Konfigurationsdatei.");
        }
    }

    // Verbindung zur Datenbank herstellen
    public static Connection getConnection() {
        try {
            Connection connection = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            System.out.println("✅ Verbindung zur Datenbank erfolgreich!");
            return connection;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Fehler bei der Datenbankverbindung.");
        }
    }
}
