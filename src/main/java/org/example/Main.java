/*
    Main JAVA
    Autor: Alessandro Keller
    Datum: 2025-15-02 09:32:17
    Kurze Beschreibung:
    Diese Datei enthält den Einstiegspunkt für das 2048-Spiel. Sie startet
    die Anwendung und initialisiert die Benutzeroberfläche und Spiellogik.
    Quellenverzeichnis:

*/

import javax.swing.SwingUtilities;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            Game game = new Game();
            UI ui = new UI(game);
            ui.setVisible(true);
        });
    }
}

