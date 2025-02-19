/*
    Tile JAVA
    Autor: Alessandro Keller
    Datum: 2025-15-02 09:32:17
    Kurze Beschreibung:
    Diese Datei repräsentiert eine einzelne Kachel im 2048-Spiel. Jede
    Kachel hat einen Wert und enthält Methoden zur Verwaltung und Darstellung
    dieses Wertes.
    Quellenverzeichnis:

*/

public class Tile {
    private int value;

    public Tile() {
        this.value = 0; // Standardwert ist 0 (leere Kachel)
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

