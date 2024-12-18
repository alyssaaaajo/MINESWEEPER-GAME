import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.io.IOException;

public class Minesweeper extends JFrame {
    private int numRows = 10;  // Number of rows
    private int numCols = 10;  // Number of columns
    private int numMines = 20; // Number of mines
    private MineTile[][] board = new MineTile[numRows][numCols];
    private JPanel gamePanel;
    private boolean gameOver = false;

    public Minesweeper() {
        setTitle("MinesweeperS - Treasure Hunt Mode");
        setSize(700, 700); // Set fixed window size
        setResizable(false); // Disable resizing
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        // Set Background Image
        setContentPane(new BackgroundPanel());

        // Title and Control Panel
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout());

        JLabel title = new JLabel("Minesweepers - Treasure Hunt Mode");
        title.setFont(loadCustomFont(16f));
        title.setForeground(Color.BLACK);

        JComboBox<String> difficulty = new JComboBox<>(new String[]{"Easy", "Medium", "Hard"});
        difficulty.setFont(loadCustomFont(14f));

        JButton startButton = new JButton("Start Game");
        startButton.setFont(loadCustomFont(14f));
        JButton restartButton = new JButton("Restart Game");
        restartButton.setFont(loadCustomFont(14f));

        titlePanel.add(title);
        title.setPreferredSize(new Dimension(200, 10)); // Adjust size of the title label
        titlePanel.add(new JLabel(" Select Difficulty: "));
        titlePanel.add(difficulty);
        titlePanel.add(startButton);
        titlePanel.add(restartButton);
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER, 20, 20));
        add(titlePanel, BorderLayout.NORTH);

        // Game Board
        gamePanel = new JPanel();
        gamePanel.setLayout(new GridLayout(numRows, numCols, 5, 5)); // Adjust grid gap
        initializeBoard();

        JPanel centerPanel = new JPanel(new GridBagLayout());
        centerPanel.add(gamePanel);
        add(centerPanel, BorderLayout.CENTER);

        // Button Listeners
        startButton.addActionListener(e -> resetGame());
        restartButton.addActionListener(e -> resetGame());

        setVisible(true);
    }

    private void initializeBoard() {
        gamePanel.removeAll();
        gameOver = false;
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                board[row][col] = new MineTile(row, col);
                gamePanel.add(board[row][col]);

                int currentRow = row;
                int currentCol = col;

                board[row][col].addMouseListener(new MouseAdapter() {
                    public void mousePressed(MouseEvent e) {
                        if (gameOver) return; // Prevent clicks after Game Over
                        if (SwingUtilities.isLeftMouseButton(e)) {
                            revealTile(currentRow, currentCol);
                        }
                    }
                });
            }
        }
        placeMines();
        gamePanel.revalidate();
        gamePanel.repaint();
    }

    private void resetGame() {
        initializeBoard();
    }

    private void placeMines() {
        int placedMines = 0;
        while (placedMines < numMines) {
            int row = (int) (Math.random() * numRows);
            int col = (int) (Math.random() * numCols);

            if (!board[row][col].isMine) {
                board[row][col].isMine = true;
                placedMines++;
            }
        }
    }

    private void revealTile(int row, int col) {
        if (board[row][col].revealed) return;

        if (board[row][col].isMine) {
            gameOver = true;
            board[row][col].revealMine();
            revealAllMines();
            JOptionPane.showMessageDialog(this, "Game Over! You hit a mine 💣", "Game Over", JOptionPane.ERROR_MESSAGE);
            return;
        }

        int mineCount = countAdjacentMines(row, col);
        board[row][col].revealTile(mineCount);

        if (mineCount == 0) {
            for (int dr = -1; dr <= 1; dr++) {
                for (int dc = -1; dc <= 1; dc++) {
                    int newRow = row + dr;
                    int newCol = col + dc;
                    if (isInBounds(newRow, newCol) && !(dr == 0 && dc == 0)) {
                        revealTile(newRow, newCol);
                    }
                }
            }
        }
    }

    private void revealAllMines() {
        for (int row = 0; row < numRows; row++) {
            for (int col = 0; col < numCols; col++) {
                if (board[row][col].isMine) {
                    board[row][col].revealMine();
                }
            }
        }
    }

    private int countAdjacentMines(int row, int col) {
        int count = 0;
        for (int dr = -1; dr <= 1; dr++) {
            for (int dc = -1; dc <= 1; dc++) {
                int newRow = row + dr;
                int newCol = col + dc;
                if (isInBounds(newRow, newCol) && board[newRow][newCol].isMine) {
                    count++;
                }
            }
        }
        return count;
    }

    private boolean isInBounds(int row, int col) {
        return row >= 0 && row < numRows && col >= 0 && col < numCols;
    }

    // Load Custom Font
    private Font loadCustomFont(float size) {
        try {
            File fontFile = new File("C:\\Users\\USER\\OneDrive\\Desktop\\minesweeper\\GamjaFlower-Regular.ttf");
            Font font = Font.createFont(Font.TRUETYPE_FONT, fontFile);
            return font.deriveFont(size);
        } catch (IOException | FontFormatException e) {
            e.printStackTrace();
            return new Font("Arial", Font.PLAIN, (int) size); // Fallback font
        }
    }

    // Custom JPanel for background image
    class BackgroundPanel extends JPanel {
        private Image backgroundImage;

        public BackgroundPanel() {
            try {
                backgroundImage = new ImageIcon("ocean.gif").getImage(); // Update with your image path
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            if (backgroundImage != null) {
                g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
            }
        }
    }

    class MineTile extends JButton {
        int row, col;
        boolean isMine;
        boolean revealed;

        public MineTile(int row, int col) {
            this.row = row;
            this.col = col;
            this.isMine = false;
            this.revealed = false;
            setFont(new Font("Arial", Font.BOLD, 20)); // Increased font size for readability
            setFocusPainted(false);
            setPreferredSize(new Dimension(50, 50)); // Set a larger tile size
            setBackground(new Color(120, 180, 120));
        }

        public void revealTile(int mineCount) {
            if (revealed) return;
            revealed = true;
            setBackground(new Color(173, 216, 230));
            if (mineCount > 0) {
                setText(String.valueOf(mineCount));
                setForeground(getNumberColor(mineCount));
            }
        }

        public void revealMine() {
            setText("💣");
            setBackground(Color.RED);
            setForeground(Color.WHITE);
        }

        private Color getNumberColor(int count) {
            switch (count) {
                case 1: return Color.BLUE;
                case 2: return Color.GREEN;
                case 3: return Color.RED;
                case 4: return new Color(128, 0, 128);
                case 5: return new Color(128, 0, 0);
                case 6: return Color.CYAN;
                case 7: return Color.BLACK;
                case 8: return Color.GRAY;
                default: return Color.BLACK;
            }
        }
    }

    public static void main(String[] args) {
        new Minesweeper();
    }
}
