package org.example.User;



import java.util.HashMap;

public class AuthSystem {
    private static HashMap<String, User> users = new HashMap<>(); // Speichert Benutzer durch ihren Benutzernamen

    static {
        // Beispielbenutzer hinzufügen
        users.put("john", new User("john", "pass123"));
        users.put("jane", new User("jane", "password"));
    }

    // Methode für den Login-Prozess
    public static User login(String username, String password) {
        User user = users.get(username);
        if (user != null && user.getPassword().equals(password)) {
            System.out.println("✅ Login erfolgreich!");
            return user;  // Erfolgreich eingeloggt, gebe den User zurück
        } else {
            System.out.println("❌ Ungültiger Benutzername oder Passwort.");
            return null;  // Login fehlgeschlagen
        }
    }

    public static boolean register(String username, String password) {
        if (users.containsKey(username)) {
            return false; // Benutzername existiert bereits
        }
        users.put(username, new User(username, password)); // Benutzer registrieren
        return true; // Erfolgreiche Registrierung
    }
}




