package Warenwirtschaft.products.user;

import org.mindrot.jbcrypt.BCrypt;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthSystem {

    public static boolean register(String username, String password, String email, String phone) {
        String checkSql = "SELECT username FROM users WHERE username = ? OR email = ?";
        String insertSql = "INSERT INTO users (username, password, email, phone) VALUES (?, ?, ?, ?)";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {

            checkStmt.setString(1, username);
            checkStmt.setString(2, email);
            ResultSet rs = checkStmt.executeQuery();

            if (rs.next()) {
                return false; // Username oder Email existiert schon
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {

            // Passwort hashen mit BCrypt
            String hashedPassword = BCrypt.hashpw(password, BCrypt.gensalt());

            insertStmt.setString(1, username);
            insertStmt.setString(2, hashedPassword);
            insertStmt.setString(3, email);
            insertStmt.setString(4, phone);

            insertStmt.executeUpdate();
            return true;

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    public static User login(String username, String password) {
        String selectSql = "SELECT * FROM users WHERE username = ?";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(selectSql)) {

            stmt.setString(1, username);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedPassword = rs.getString("password");

                // Passwortprüfung mit BCrypt
                if (BCrypt.checkpw(password, storedPassword)) {
                    int id = rs.getInt("id");
                    String dbUsername = rs.getString("username");
                    String dbEmail = rs.getString("email");
                    String dbPhone = rs.getString("phone");
                    boolean isAdmin = rs.getBoolean("is_admin");

                    return new User(id, dbUsername, storedPassword, dbEmail, dbPhone, isAdmin);
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;
    }
}
