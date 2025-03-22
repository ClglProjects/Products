package Warenwirtschaft.produkte.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSystem {

    /**
     * Registriert einen neuen Benutzer.
     * Gibt true zurück, wenn es geklappt hat,
     * sonst false (z. B. wenn Benutzername oder E-Mail bereits existieren).
     */
    public static boolean register(String username, String password, String email, String phone) {
        String checkSql = "SELECT username FROM users WHERE username = ? OR email = ?";
        String insertSql = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";

        // 1) Prüfen, ob Username oder E-Mail schon existieren
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                // Benutzername oder E-Mail ist schon vergeben
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // 2) Einfügen
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            insertStmt.setString(1, username);
            insertStmt.setString(2, password);  // Hinweis: In echten Projekten -> Passwort-Hash
            insertStmt.setString(3, email);
            insertStmt.setString(4, phone);

            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Versucht, den Benutzer per (username, password) einzuloggen.
     * Gibt bei Erfolg ein User-Objekt zurück, sonst null.
     */
    public static User login(String username, String password) {
        String selectSql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");
                if (storedPassword.equals(password)) {
                    // Passwörter stimmen überein, erstelle User-Objekt mit Admin-Status
                    int id = rs.getInt("id");
                    String dbUsername = rs.getString("username");
                    String dbEmail = rs.getString("email");
                    String dbPhone = rs.getString("phone");
                    boolean isAdmin = rs.getBoolean("is_admin");  // Holt den Admin-Status aus der DB

                    return new User(id, dbUsername, storedPassword, dbEmail, dbPhone, isAdmin);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Kein Benutzer gefunden oder Passwort falsch
        return null;
    }
}
