public class Game {
    private Board board;
    private int score;

    public Game() {
        board = new Board();
        score = 0;
    }

    public Board getBoard() {
        return board;
    }

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

    public void resetGame() {
        board = new Board();
        score = 0;
    }
}

