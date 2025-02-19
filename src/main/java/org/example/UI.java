/*
    UI JAVA
    Author: Alessandro Keller
    Short Description:
    This class provides the graphical user interface for the 2048 game. It manages displaying the game,
    including the game board, score, new game buttons, and tile animations.
    References:
*/

// This file was made with the help of AI, especially the complicated logics


// Imports all the librarys
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.HashMap;
import java.util.Map;

public class UI extends JFrame {


    private Game game;
    private JPanel boardPanel;
    private JLabel scoreLabel;
    private JLabel bestScoreLabel;
    private JLabel titleLabel;
    private JLabel subtitleLabel;
    private JButton newGameButton;
    private int highScore = 0;
    private boolean isAnimating = false;
    private long lastMoveTime = 0;
    private final long MOVE_DELAY = 200;
    private JDialog gameOverDialog;
    private Timer gameOverTimer;

    private static final int CELL_SIZE = 80;
    private static final int BOARD_SIZE = 4 * CELL_SIZE;
    private static final int ANIMATION_DURATION = 300;
    private static final float EASE_FACTOR = 2.0f;
    private static final int ANIMATION_FPS = 60;
    private static final int ANIMATION_FRAME_TIME = 60 / ANIMATION_FPS;
    private Map<Point, AnimatedTile> animatedTiles = new HashMap<>();

    public UI(Game game) { //Sets up the main game window with title, score displays, and layout
        this.game = game;
        setTitle("2048");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());
        getContentPane().setBackground(new Color(255, 248, 239));

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBackground(new Color(255, 251, 240));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setOpaque(false);

        titleLabel = new JLabel("2048");
        titleLabel.setFont(new Font("Arial", Font.BOLD, 48));
        titleLabel.setForeground(new Color(239, 211, 118));

        JPanel scorePanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        scorePanel.setOpaque(false);

        scoreLabel = createScoreLabel("SCORE");
        bestScoreLabel = createScoreLabel("BEST");

        scorePanel.add(scoreLabel);
        scorePanel.add(bestScoreLabel);

        headerPanel.add(titleLabel, BorderLayout.WEST);
        headerPanel.add(scorePanel, BorderLayout.EAST);


        // Creates and configures the New Game button and game board panel
        newGameButton = new JButton("New Game");
        newGameButton.setFont(new Font("Arial", Font.BOLD, 16));
        newGameButton.setBackground(new Color(250, 243, 217));
        newGameButton.setForeground(Color.BLACK);
        newGameButton.setFocusPainted(false);
        newGameButton.addActionListener(e -> resetGame());

        JPanel subHeaderPanel = new JPanel(new BorderLayout());
        subHeaderPanel.setOpaque(false);
        subHeaderPanel.add(newGameButton, BorderLayout.EAST);

        boardPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                drawBoard(g);
            }
        };

        boardPanel.setPreferredSize(new Dimension(BOARD_SIZE, BOARD_SIZE));
        boardPanel.setBackground(new Color(187,173,160));

        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(subHeaderPanel, BorderLayout.CENTER);
        mainPanel.add(boardPanel, BorderLayout.SOUTH);

        add(mainPanel);

        createGameOverDialog();

        addKeyListener(new KeyAdapter() { // adds a keylistener that responds to key press events
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        handleMove("UP");
                        break;
                    case KeyEvent.VK_DOWN:
                        handleMove("DOWN");
                        break;
                    case KeyEvent.VK_LEFT:
                        handleMove("LEFT");
                        break;
                    case KeyEvent.VK_RIGHT:
                        handleMove("RIGHT");
                        break;
                }
            }
        });

        setFocusable(true);
        pack();
        setLocationRelativeTo(null);
        updateUI();
    }

    // Creates a styled score display label with title and initial value of 0
    private JLabel createScoreLabel(String title) {
        JLabel label = new JLabel("<html><center>" + title + "<br>0</center></html>");
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.WHITE);
        label.setBackground(new Color(187, 173, 160));
        label.setOpaque(true);
        label.setBorder(BorderFactory.createEmptyBorder(5, 15, 5, 15));
        label.setHorizontalAlignment(JLabel.CENTER);
        return label;
    }

    private void drawBoard(Graphics g) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setRenderingHint(RenderingHints.KEY_RENDERING, RenderingHints.VALUE_RENDER_QUALITY);

        // Draw cells and regular tiles
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int x = j * CELL_SIZE;
                int y = i * CELL_SIZE;

                // Cell background
                g2d.setColor(new Color(204, 192, 179));
                g2d.fillRoundRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, 10, 10);

                // Regular tile
                Tile tile = game.getBoard().getTile(i, j);
                if (tile.getValue() != 0 && !animatedTiles.containsKey(new Point(j, i))) {
                    drawTile(g2d, tile.getValue(), x, y);
                }
            }
        }

        // Draw animated tiles
        for (AnimatedTile tile : animatedTiles.values()) {
            drawTile(g2d, tile.getValue(), tile.getCurrentX(), tile.getCurrentY());
        }
    }

    private void drawTile(Graphics2D g2d, int value, int x, int y) {
        Color bgColor = getBackgroundColor(value);
        g2d.setColor(bgColor);
        g2d.fillRoundRect(x + 5, y + 5, CELL_SIZE - 10, CELL_SIZE - 10, 10, 10);

        g2d.setColor(getForegroundColor(value));
        String s = String.valueOf(value);
        Font font = new Font("Arial", Font.BOLD, getFontSize(value));
        g2d.setFont(font);

        FontMetrics fm = g2d.getFontMetrics();
        int textWidth = fm.stringWidth(s);
        int textHeight = fm.getHeight();
        int textX = x + (CELL_SIZE - textWidth) / 2;
        int textY = y + (CELL_SIZE - textHeight) / 2 + fm.getAscent();

        g2d.drawString(s, textX, textY);
    }

    private int getFontSize(int value) {
        String s = String.valueOf(value);
        if (s.length() <= 2) return 32;
        if (s.length() == 3) return 28;
        return 22;
    }

    private void updateUI() {
        scoreLabel.setText("<html><center>SCORE<br>" + game.getScore() + "</center></html>");
        if (game.getScore() > highScore) {
            highScore = game.getScore();
        }
        bestScoreLabel.setText("<html><center>BEST<br>" + highScore + "</center></html>");
        boardPanel.repaint();
    }

    // sets the color for every tile
    private Color getBackgroundColor(int value) {
        switch (value) {
            case 2: return new Color(238, 228, 218);
            case 4: return new Color(237, 224, 200);
            case 8: return new Color(242, 177, 121);
            case 16: return new Color(245, 149, 99);
            case 32: return new Color(246, 124, 95);
            case 64: return new Color(246, 94, 59);
            case 128: return new Color(237, 207, 114);
            case 256: return new Color(237, 204, 97);
            case 512: return new Color(237, 200, 80);
            case 1024: return new Color(237, 197, 63);
            case 2048: return new Color(237, 194, 46);
            default: return new Color(60, 58, 50);
        }
    }

    private Color getForegroundColor(int value) {
        return value <= 4 ? new Color(119, 110, 101) : new Color(249, 246, 242);
    }

    private void handleMove(String direction) {
        if (isAnimating) return;

        long currentTime = System.currentTimeMillis();
        if (currentTime - lastMoveTime < MOVE_DELAY) return;



        isAnimating = true;
        lastMoveTime = currentTime;

        // Store old board state
        int[][] oldBoard = new int[4][4];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                oldBoard[i][j] = game.getBoard().getTile(i, j).getValue();
            }
        }

        // Create a list of all tiles that are moving
        Map<Point, Point> tileMovement = new HashMap<>();

        game.move(direction);

        // Create animated tiles (for all moves, not just merges)
        animatedTiles.clear();
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                int newValue = game.getBoard().getTile(i, j).getValue();
                if (newValue != 0 && newValue != oldBoard[i][j]) {
                    Point target = new Point(j, i);
                    Point source = findSource(oldBoard, newValue, target, direction);
                    if (source != null) {
                        animatedTiles.put(target, new AnimatedTile(newValue, source, target));
                        tileMovement.put(source, target);
                    }
                }
            }
        }

        // Animate all tile movements
        Timer animationTimer = new Timer(ANIMATION_FRAME_TIME, new ActionListener() {
            long startTime = System.currentTimeMillis();

            @Override
            public void actionPerformed(ActionEvent e) {
                long elapsed = System.currentTimeMillis() - startTime;
                float progress = Math.min(1f, (float) elapsed / ANIMATION_DURATION);
                float easedProgress = calculateEasing(progress);

                boolean allFinished = true;
                for (AnimatedTile tile : animatedTiles.values()) {
                    tile.updatePosition(easedProgress);
                    if (!tile.isFinished()) {
                        allFinished = false;
                    }
                }

                boardPanel.repaint();

                if (allFinished) {
                    ((Timer) e.getSource()).stop();
                    animatedTiles.clear();
                    isAnimating = false;

                    if (game.getBoard().isGameOver()) {
                        showGameOverAndRestart();
                    }
                }
            }
        });
        animationTimer.start();

        updateUI();
    }

    private float calculateEasing(float progress) {
        return 1 - (float)Math.pow(1 - progress, 3);
    }

    private Point findSource(int[][] oldBoard, int value, Point target, String direction) {
        int dx = 0, dy = 0;
        switch (direction) {
            case "UP": dy = 1; break;
            case "DOWN": dy = -1; break;
            case "LEFT": dx = 1; break;
            case "RIGHT": dx = -1; break;
        }

        int x = target.x, y = target.y;
        while (true) {
            x += dx;
            y += dy;
            if (x < 0 || x >= 4 || y < 0 || y >= 4) break;
            if (oldBoard[y][x] == value) return new Point(x, y);
            if (oldBoard[y][x] != 0) break;
        }
        return null;
    }

    private void createGameOverDialog() {
        gameOverDialog = new JDialog(this, "Game Over", true);
        gameOverDialog.setLayout(new BorderLayout());

        JLabel gameOverLabel = new JLabel("Game Over!", SwingConstants.CENTER);
        gameOverLabel.setFont(new Font("Arial", Font.BOLD, 32));
        gameOverLabel.setForeground(Color.WHITE);
        gameOverLabel.setOpaque(true);
        gameOverLabel.setBackground(new Color(187, 173, 160));

        JButton restartButton = new JButton("Reset Game");
        restartButton.setFont(new Font("Arial", Font.BOLD, 20));
        restartButton.setBackground(new Color(143, 122, 102));
        restartButton.setForeground(Color.WHITE);
        restartButton.setFocusPainted(false);
        restartButton.setBorder(BorderFactory.createEmptyBorder(15, 30, 15, 30));
        restartButton.addActionListener(e -> {
            resetGame();
            gameOverDialog.dispose();
        });

        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(218, 208, 198));
        buttonPanel.add(restartButton);

        gameOverDialog.add(gameOverLabel, BorderLayout.CENTER);
        gameOverDialog.add(buttonPanel, BorderLayout.SOUTH);
        gameOverDialog.setSize(400, 200);
        gameOverDialog.setLocationRelativeTo(this);
    }

    private void showGameOverAndRestart() {
        if (gameOverDialog == null) {
            createGameOverDialog();
        }
        gameOverDialog.setVisible(true);

        gameOverTimer = new Timer(3000, e -> {
            gameOverDialog.setVisible(false);
            resetGame();
            gameOverTimer.stop();
        });
        gameOverTimer.setRepeats(false);
        gameOverTimer.start();
    }

    private void resetGame() {
        game = new Game();  // Create a completely new game instance
        updateUI();
        boardPanel.repaint();
        this.requestFocus();  // Make sure the UI window has focus
    }

    private class AnimatedTile {
        private int value;
        private Point source;
        private Point target;
        private float currentX;
        private float currentY;

        public AnimatedTile(int value, Point source, Point target) {
            this.value = value;
            this.source = source;
            this.target = target;
            this.currentX = source.x * CELL_SIZE;
            this.currentY = source.y * CELL_SIZE;
        }

        public void updatePosition(float progress) {
            currentX = source.x * CELL_SIZE + (target.x * CELL_SIZE - source.x * CELL_SIZE) * progress;
            currentY = source.y * CELL_SIZE + (target.y * CELL_SIZE - source.y * CELL_SIZE) * progress;
        }

        public boolean isFinished() {
            return Math.abs(currentX - target.x * CELL_SIZE) < 1 &&
                    Math.abs(currentY - target.y * CELL_SIZE) < 1;
        }

        public int getValue() { return value; }
        public int getCurrentX() { return (int) currentX; }
        public int getCurrentY() { return (int) currentY; }
    }
}

