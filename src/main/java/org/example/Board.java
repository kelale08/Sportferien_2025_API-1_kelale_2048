import java.util.Random;

public class Board {
    private Tile[][] tiles;
    private Random random;

    public Board() {
        tiles = new Tile[4][4];
        random = new Random();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j] = new Tile();
            }
        }
        resetGame();
    }

    public Tile[][] getTiles() {
        return tiles;
    }

    public void addRandomTile() {
        int row, col;
        do {
            row = random.nextInt(4);
            col = random.nextInt(4);
        } while (tiles[row][col].getValue() != 0);
        tiles[row][col].setValue(random.nextInt(2) == 0 ? 2 : 4);
    }

    public void resetGame() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                tiles[i][j].setValue(0);
            }
        }
        addRandomTile();
        addRandomTile();
    }

    public boolean moveUp() {
        boolean moved = false;
        for (int col = 0; col < 4; col++) {
            moved |= moveColumn(col, -1);
        }
        return moved;
    }

    public boolean moveDown() {
        boolean moved = false;
        for (int col = 0; col < 4; col++) {
            moved |= moveColumn(col, 1);
        }
        return moved;
    }

    public boolean moveLeft() {
        boolean moved = false;
        for (int row = 0; row < 4; row++) {
            moved |= moveRow(row, -1);
        }
        return moved;
    }

    public boolean moveRight() {
        boolean moved = false;
        for (int row = 0; row < 4; row++) {
            moved |= moveRow(row, 1);
        }
        return moved;
    }

    private boolean moveColumn(int col, int direction) {
        boolean moved = false;
        int start = direction == -1 ? 0 : 3;
        int end = direction == -1 ? 4 : -1;
        int step = direction == -1 ? 1 : -1;

        for (int i = start; i != end; i += step) {
            int nextRow = i + direction;
            while (nextRow >= 0 && nextRow < 4) {
                if (tiles[i][col].getValue() == 0) break;
                if (tiles[nextRow][col].getValue() == 0) {
                    tiles[nextRow][col].setValue(tiles[i][col].getValue());
                    tiles[i][col].setValue(0);
                    moved = true;
                } else if (tiles[nextRow][col].getValue() == tiles[i][col].getValue()) {
                    tiles[nextRow][col].setValue(tiles[nextRow][col].getValue() * 2);
                    tiles[i][col].setValue(0);
                    moved = true;
                    break;
                } else {
                    break;
                }
                nextRow += direction;
            }
        }
        return moved;
    }

    private boolean moveRow(int row, int direction) {
        boolean moved = false;
        int start = direction == -1 ? 0 : 3;
        int end = direction == -1 ? 4 : -1;
        int step = direction == -1 ? 1 : -1;

        for (int i = start; i != end; i += step) {
            int nextCol = i + direction;
            while (nextCol >= 0 && nextCol < 4) {
                if (tiles[row][i].getValue() == 0) break;
                if (tiles[row][nextCol].getValue() == 0) {
                    tiles[row][nextCol].setValue(tiles[row][i].getValue());
                    tiles[row][i].setValue(0);
                    moved = true;
                } else if (tiles[row][nextCol].getValue() == tiles[row][i].getValue()) {
                    tiles[row][nextCol].setValue(tiles[row][nextCol].getValue() * 2);
                    tiles[row][i].setValue(0);
                    moved = true;
                    break;
                } else {
                    break;
                }
                nextCol += direction;
            }
        }
        return moved;
    }

    public Tile getTile(int row, int col) {
        return tiles[row][col];
    }

    public boolean isGameOver() {
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                if (tiles[i][j].getValue() == 0) {
                    return false;
                }
                if (i < 3 && tiles[i][j].getValue() == tiles[i + 1][j].getValue()) {
                    return false;
                }
                if (j < 3 && tiles[i][j].getValue() == tiles[i][j + 1].getValue()) {
                    return false;
                }
            }
        }
        return true;
    }


}

