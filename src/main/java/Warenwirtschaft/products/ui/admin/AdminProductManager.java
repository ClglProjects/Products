package Warenwirtschaft.products.ui.admin;

import Warenwirtschaft.products.user.DatabaseConnection;
import Warenwirtschaft.products.ui.util.ProductOptions.Farbe;
import Warenwirtschaft.products.ui.util.ProductOptions.Speicher;
import static Warenwirtschaft.products.ui.util.OptionSelector.chooseEnumOption;

import java.sql.*;
import java.util.*;

public class AdminProductManager {
    private static final Scanner scanner = new Scanner(System.in);

    public static void show() {
        String category = chooseCategory();
        if (category == null) {
            System.out.println("❌ Ungültige Kategorieauswahl.");
            return;
        }

        while (true) {
            System.out.println("\n🛠️ Produktverwaltung (" + category + "):");
            System.out.println("1⃣ Produkte anzeigen");
            System.out.println("2⃣ Produktvariante hinzufügen");
            System.out.println("3⃣ Produktvariante löschen");
            System.out.println("4⃣ Zurück");
            System.out.print("👉 Auswahl: ");
            String input = scanner.nextLine();

            switch (input) {
                case "1" -> showAllProducts(category);
                case "2" -> addProductVariant(category);
                case "3" -> deleteProductVariant(category);
                case "4" -> { return; }
                default -> System.out.println("❌ Ungültige Eingabe!");
            }
        }
    }

    private static String chooseCategory() {
        System.out.println("\n📂 Kategorie wählen:");
        System.out.println("1⃣ Handy");
        System.out.println("2⃣ Laptop");
        System.out.println("3⃣ Maus");
        System.out.print("👉 Auswahl: ");
        return switch (scanner.nextLine()) {
            case "1" -> "handy";
            case "2" -> "laptop";
            case "3" -> "mouse";
            default -> null;
        };
    }

    private static void showAllProducts(String category) {
        String variantTable = category + "_variants";
        String joinColumn = category + "_id";
        String mainTable = category + "s";

        String sql = "SELECT v.*, m.model, b.name AS brand FROM " + variantTable + " v " +
                "JOIN " + mainTable + " m ON v." + joinColumn + " = m.id " +
                "JOIN brands b ON m.brand_id = b.id";

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            System.out.println("\n📦 Alle " + category + "-Varianten:");
            ResultSetMetaData meta = rs.getMetaData();
            while (rs.next()) {
                StringBuilder output = new StringBuilder("🄚 ID: " + rs.getInt("id"));
                output.append(" | Modell: ").append(rs.getString("model"));
                output.append(" | Marke: ").append(rs.getString("brand"));
                if (hasColumn(meta, "color")) output.append(" | Farbe: ").append(rs.getString("color"));
                if (hasColumn(meta, "storage")) output.append(" | Speicher: ").append(rs.getString("storage"));
                if (hasColumn(meta, "price")) output.append(" | Preis: ").append(rs.getDouble("price")).append("€");
                if (hasColumn(meta, "stock")) output.append(" | Lager: ").append(rs.getInt("stock"));
                System.out.println(output);
            }

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Produkte.");
            e.printStackTrace();
        }
    }

    private static void addProductVariant(String category) {
        String variantTable = category + "_variants";
        String joinColumn = category + "_id";
        String mainTable = category + "s";

        showModelList(category);

        System.out.print("👉 ID des existierenden Modells (aus Tabelle " + mainTable + "): ");
        int productId;
        try {
            productId = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Ungültige ID.");
            return;
        }

        // Farben dynamisch oder neu
        String color = selectColorOption(category, productId);
        if (color == null) return;

        // Speicher prüfen, ob überhaupt vorhanden in Tabelle
        String storage = null;
        if (columnExists(variantTable, "storage")) {
            storage = selectStorageOption(category, productId);
            if (storage == null) return;
        }

        System.out.print("👉 Preis (z. B. 799.99): ");
        double price;
        try {
            price = Double.parseDouble(scanner.nextLine().replace(",", "."));
        } catch (NumberFormatException e) {
            System.out.println("❌ Ungültiger Preis.");
            return;
        }

        System.out.print("👉 Lagerbestand (Ganzzahl): ");
        int stock;
        try {
            stock = Integer.parseInt(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("❌ Ungültiger Lagerbestand.");
            return;
        }

        String sql;
        if (storage != null) {
            sql = "INSERT INTO " + variantTable + " (color, storage, price, stock, " + joinColumn + ") VALUES (?, ?, ?, ?, ?)";
        } else {
            sql = "INSERT INTO " + variantTable + " (color, price, stock, " + joinColumn + ") VALUES (?, ?, ?, ?)";
        }

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            int i = 1;
            stmt.setString(i++, color);
            if (storage != null) stmt.setString(i++, storage);
            stmt.setDouble(i++, price);
            stmt.setInt(i++, stock);
            stmt.setInt(i, productId);

            if (stmt.executeUpdate() > 0)
                System.out.println("✅ Produktvariante erfolgreich hinzugefügt.");
            else
                System.out.println("❌ Hinzufügen fehlgeschlagen.");

        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Speichern.");
            e.printStackTrace();
        }
    }

    private static String selectColorOption(String category, int productId) {
        List<String> colors = loadAvailableValuesFromDB(category + "_variants", "color", category + "_id", productId);
        if (!colors.isEmpty()) {
            System.out.println("\n🔘 Verfügbare Farben:");
            for (int i = 0; i < colors.size(); i++) {
                System.out.println((i + 1) + "️⃣ " + colors.get(i));
            }
            System.out.println("0️⃣ Neue Farbe hinzufügen");
            System.out.print("👉 Eingabe (0-" + colors.size() + "): ");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 0) {
                    System.out.print("✏️ Neue Farbe eingeben: ");
                    return scanner.nextLine().trim();
                } else if (input >= 1 && input <= colors.size()) {
                    return colors.get(input - 1);
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static String selectStorageOption(String category, int productId) {
        List<String> options = loadAvailableValuesFromDB(category + "_variants", "storage", category + "_id", productId);
        if (!options.isEmpty()) {
            System.out.println("\n🔘 Verfügbare Speichergrößen:");
            for (int i = 0; i < options.size(); i++) {
                System.out.println((i + 1) + "️⃣ " + options.get(i));
            }
            System.out.println("0️⃣ Neue Speichergröße eingeben");
            System.out.print("👉 Eingabe (0-" + options.size() + "): ");
            try {
                int input = Integer.parseInt(scanner.nextLine());
                if (input == 0) {
                    System.out.print("✏️ Neue Speichergröße (z. B. 512GB SSD): ");
                    return scanner.nextLine().trim();
                } else if (input >= 1 && input <= options.size()) {
                    return options.get(input - 1);
                }
            } catch (Exception ignored) {}
        }
        return null;
    }

    private static List<String> loadAvailableValuesFromDB(String table, String column, String joinColumn, int productId) {
        List<String> values = new ArrayList<>();
        String sql = "SELECT DISTINCT " + column + " FROM " + table + " WHERE " + joinColumn + " = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, productId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) values.add(rs.getString(column));
        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden von " + column + ": " + e.getMessage());
        }
        return values;
    }

    private static boolean columnExists(String tableName, String columnName) {
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement("SELECT * FROM " + tableName + " LIMIT 1");
             ResultSet rs = stmt.executeQuery()) {

            ResultSetMetaData meta = rs.getMetaData();
            for (int i = 1; i <= meta.getColumnCount(); i++) {
                if (meta.getColumnName(i).equalsIgnoreCase(columnName)) return true;
            }
        } catch (SQLException e) {
            System.out.println("❌ Fehler bei Spaltenprüfung: " + e.getMessage());
        }
        return false;
    }

    private static void deleteProductVariant(String category) {
        System.out.print("👉 Variante-ID zum Löschen: ");
        try {
            int id = Integer.parseInt(scanner.nextLine());
            String sql = "DELETE FROM " + category + "_variants WHERE id = ?";
            try (Connection conn = DatabaseConnection.getConnection();
                 PreparedStatement stmt = conn.prepareStatement(sql)) {
                stmt.setInt(1, id);
                int rows = stmt.executeUpdate();
                System.out.println(rows > 0 ? "✅ Variante gelöscht." : "❌ Keine Variante mit dieser ID.");
            }
        } catch (Exception e) {
            System.out.println("❌ Fehler beim Löschen.");
        }
    }

    private static void showModelList(String category) {
        String sql = "SELECT m.id, m.model, b.name AS brand FROM " + category + "s m " +
                "JOIN brands b ON m.brand_id = b.id";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            System.out.println("\n📋 Verfügbare Modelle:");
            while (rs.next()) {
                System.out.println("🄚 ID: " + rs.getInt("id") +
                        " | Modell: " + rs.getString("model") +
                        " | Marke: " + rs.getString("brand"));
            }
        } catch (SQLException e) {
            System.out.println("❌ Fehler beim Laden der Modelle.");
        }
    }

    private static boolean hasColumn(ResultSetMetaData meta, String columnName) throws SQLException {
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            if (meta.getColumnName(i).equalsIgnoreCase(columnName)) return true;
        }
        return false;
    }
}
