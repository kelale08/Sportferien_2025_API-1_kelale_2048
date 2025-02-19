/*
    Tile JAVA
    Author: Alessandro Keller
    Short Description:
    This file represents a single tile in the 2048 game. Each tile has a value
    and includes methods for managing and displaying this value.
*/

// This file was made with the help of AI, especially the complicated logics

public class Tile {
    private int value;

    public Tile() {
        this.value = 0;  // Empty tile has value 0
    }

    public int getValue() {
        return value;
    }

    public void setValue(int value) {
        this.value = value;
    }
}

