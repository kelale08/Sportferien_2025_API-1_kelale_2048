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
        return move(-1, 0);
    }

    public boolean moveDown() {
        return move(1, 0);
    }

    public boolean moveLeft() {
        return move(0, -1);
    }

    public boolean moveRight() {
        return move(0, 1);
    }

    private boolean move(int rowDirection, int colDirection) {
        boolean moved = false;
        int start = (rowDirection == 1 || colDirection == 1) ? 3 : 0;
        int end = (rowDirection == 1 || colDirection == 1) ? -1 : 4;
        int step = (rowDirection == 1 || colDirection == 1) ? -1 : 1;

        for (int i = start; i != end; i += step) {
            for (int j = start; j != end; j += step) {
                int row = rowDirection == 0 ? i : j;
                int col = colDirection == 0 ? i : j;
                if (tiles[row][col].getValue() != 0) {
                    moved |= moveTile(row, col, rowDirection, colDirection);
                }
            }
        }

        return moved;
    }

    private boolean moveTile(int row, int col, int rowDirection, int colDirection) {
        int newRow = row;
        int newCol = col;
        boolean merged = false;
        boolean moved = false;

        while (true) {
            int nextRow = newRow + rowDirection;
            int nextCol = newCol + colDirection;

            if (nextRow < 0 || nextRow >= 4 || nextCol < 0 || nextCol >= 4) {
                break;
            }

            if (tiles[nextRow][nextCol].getValue() == 0) {
                newRow = nextRow;
                newCol = nextCol;
                moved = true;
            } else if (!merged && tiles[nextRow][nextCol].getValue() == tiles[row][col].getValue()) {
                newRow = nextRow;
                newCol = nextCol;
                merged = true;
                moved = true;
            } else {
                break;
            }
        }

        if (moved) {
            if (merged) {
                tiles[newRow][newCol].setValue(tiles[row][col].getValue() * 2);
            } else {
                tiles[newRow][newCol].setValue(tiles[row][col].getValue());
            }
            tiles[row][col].setValue(0);
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

