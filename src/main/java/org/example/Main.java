/*
    Main JAVA
    Author: Alessandro Keller
    Short Description:
    This file contains the entry point for the 2048 game. It launches
    the application and initializes the user interface and game logic.
*/

// This file was made with the help of AI, especially the complicated logics

// Creates and displays the game window on the event dispatch thread
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

