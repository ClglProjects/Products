# Benutzerverwaltung mit iPhone-Modellen - Java und Arraylist(Datenbank integration..)

## 📌 Projektbeschreibung

Dieses Projekt zeigt die Entwicklung einer Java-Anwendung, die eine iPhone-Verwaltung mit Modellen, Beständen und Preisangaben ermöglicht. Die Anwendung bietet dem Benutzer die Möglichkeit, iPhones anzuzeigen, Bestände zu reduzieren (z. B. nach einem Kauf) und Modelle anhand von Benutzereingaben zu finden, wobei auch Tippfehler automatisch berücksichtigt werden.

Das Projekt nutzt eine **ArrayList** zur Speicherung der iPhone-Objekte und implementiert eine Funktion zur Berechnung der Levenshtein-Distanz, um ähnliche Modelle zu finden, falls der Benutzer einen Tippfehler macht.

Verwendete Technologien:
- **Java (Version 21):** Hauptsprache zur Entwicklung der Anwendung.
- **Levenshtein-Distanz:** Zur Fehlerkorrektur bei Benutzereingaben und zum Finden von ähnlich geschriebenen Modellen.
- **ArrayList:** Speicherung und Verwaltung von iPhone-Modellen und deren Beständen.
- **Scanner:** Interaktive Benutzereingabe im Terminal.

 Projektziele:
Das Projekt demonstriert die Fähigkeit, mit Java eine einfache, benutzerfreundliche iPhone-Verwaltung zu erstellen, die auch in realen Szenarien, wie z. B. in einem Warenkorb-System oder Inventarsystem, nützlich sein kann.

 Funktionen

- **iPhone-Modelle anzeigen:** Zeigt eine Liste aller iPhones mit Name, Modell, Preis und Verfügbarkeit an.
- **Bestand reduzieren:** Ermöglicht das Reduzieren des Bestands um 1 (z. B. nach einem Kauf).
- **Fehlerkorrektur:** Wenn der Benutzer ein Modell sucht, wird anhand der Levenshtein-Distanz nach ähnlichen Modellen gesucht, um Tippfehler zu korrigieren.
- **Interaktive Eingabe:** Nutzer können nach Modellen suchen und erhalten Feedback, ob die Eingabe korrekt war.

Technologien und Tools

- **Java (JDK 21):** Zum Entwickeln und Ausführen des Programms.
- **LevenshteinDistance aus Apache Commons Text:** Wird verwendet, um die Eingabe des Benutzers mit den Modellen abzugleichen und Tippfehler zu erkennen.
- **ArrayList:** Dynamische Datenstruktur zur Speicherung der iPhones und ihrer Bestände.
- **Scanner:** Ermöglicht Benutzereingaben zur Interaktion mit der Anwendung.


