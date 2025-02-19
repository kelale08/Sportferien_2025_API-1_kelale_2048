/*
    Game JAVA
    Author: Alessandro Keller
    Short Description:
    This file contains the game logic for the 2048 game. It manages game functions such as moves,
    score calculation, and resetting the game.
*/

// This file was made with the help of AI, especially the complicated logics


public class Game {
    private Board board;
    private int score;

    // Initialize board and score
    public Game() {
        board = new Board();
        score = 0;
    }

    // Get board
    public Board getBoard() {
        return board;
    }

    // Get score
    public int getScore() {
        return score;
    }


    public boolean move(String direction) {
        boolean moved = false;

        switch (direction) {
            case "UP":
                moved = board.moveUp();
                break;
            case "DOWN":
                moved = board.moveDown();
                break;
            case "LEFT":
                moved = board.moveLeft();
                break;
            case "RIGHT":
                moved = board.moveRight();
                break;
        }
        if (moved) {
            updateScore();
            board.addRandomTile();
        }

        return moved;
    }


    private void updateScore() {
        int newScore = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                newScore += board.getTile(i, j).getValue();
            }
        }
        score = newScore;
    }


    // Reset game
    public void resetGame() {
        board = new Board();
        score = 0;
    }
}

