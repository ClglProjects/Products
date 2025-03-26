package Warenwirtschaft.products.ui.customer;

import Warenwirtschaft.products.user.AuthSystem;
import Warenwirtschaft.products.user.User;

import java.util.Scanner;

public class AuthMenu {

    public static User login(Scanner scanner) {
        System.out.print("👉 Benutzername: ");
        String username = scanner.nextLine();
        System.out.print("👉 Passwort: ");
        String password = scanner.nextLine();

        User user = AuthSystem.login(username, password);
        if (user != null) {
            System.out.println("✅ Login erfolgreich.");
        } else {
            System.out.println("❌ Benutzername oder Passwort falsch.");
        }
        return user;
    }

    public static User register(Scanner scanner) {
        System.out.print("👉 Benutzername: ");
        String username = scanner.nextLine();

        String password, confirmPassword;
        while (true) {
            System.out.print("👉 Passwort: ");
            password = scanner.nextLine();
            System.out.print("🔁 Passwort wiederholen: ");
            confirmPassword = scanner.nextLine();
            if (!password.equals(confirmPassword)) {
                System.out.println("❌ Passwörter stimmen nicht überein.");
            } else {
                break;
            }
        }

        System.out.print("👉 E-Mail: ");
        String email = scanner.nextLine();

        System.out.print("👉 Telefonnummer (optional): ");
        String phone = scanner.nextLine();

        boolean success = AuthSystem.register(username, password, email, phone);
        if (success) {
            System.out.println("✅ Registrierung erfolgreich!");
            return AuthSystem.login(username, password);
        } else {
            System.out.println("❌ Benutzername oder E-Mail existiert bereits!");
            return null;
        }
    }
}
