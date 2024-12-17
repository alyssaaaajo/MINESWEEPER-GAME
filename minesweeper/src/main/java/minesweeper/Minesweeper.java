package minesweeper;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper extends JPanel {
    private class MineTile extends JButton {
        int r;
        int c;

        public MineTile(int r, int c) {
            this.r = r;
            this.c = c;
        }
    }

    int tileSize = 70;
    int numRows = 8;
    int numCols = numRows;
    int boardWidth = numCols * tileSize;
    int boardHeight = numRows * tileSize;
    // int boardWidth = 1200;
    // int boardHeight = 600;

    JFrame frame = new JFrame("Minesweeper");
    JLabel textLabel = new JLabel();
    JPanel textPanel = new JPanel();
    JPanel boardPanel = new JPanel();

    MineTile[][] board = new MineTile[numRows][numCols];
    ArrayList<MineTile> mineList;

    // Exceptionms, abstraction, encapsulation, inheritance, polymorphism, interfaec

    Image backgroundImg;

    public Minesweeper() {
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setLocationRelativeTo(null);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());

        textLabel.setFont(new Font("Arial", Font.BOLD, 25));
        textLabel.setHorizontalAlignment(JLabel.CENTER);
        textLabel.setText("Minesweeper");
        textLabel.setOpaque(true);

        textPanel.setLayout(new BorderLayout());
        textPanel.add(textLabel);
        frame.add(textPanel, BorderLayout.NORTH);

        boardPanel.setLayout(new GridLayout(numRows, numCols)); // 8x8
        // boardPanel.setBackground(Color.green);
        frame.add(boardPanel);

        for (int r = 0; r < numRows; r++) {
            for (int c = 0; c < numCols; c++) {
                MineTile tile = new MineTile(r, c);
                board[r][c] = tile;

                tile.setFocusable(false);
                tile.setMargin(new Insets(0, 0, 0, 0));
                tile.setFont(new Font("Arial Unicode MS", Font.PLAIN, 45));
                // tile.setText("💣");
                tile.addMouseListener(new MouseAdapter() {
                    @Override
                    public void mousePressed(MouseEvent e) {
                        MineTile tile = (MineTile) e.getSource();

                        // left click
                        if (e.getButton() == MouseEvent.BUTTON1) {
                            if (tile.getText() == "") {
                                if (mineList.contains(tile)) {
                                    revealMines();
                                }
                            }
                        }
                    }
                });
                boardPanel.add(tile);
            }
        }

        frame.setVisible(true);

        setMines();

        // setPreferredSize(new Dimension(boardWidth, boardHeight));
        // setFocusable(true);

        // backgroundImg = new ImageIcon(getClass().getResource("./ocean.gif")).getImage();
    }

    public void setMines() {
        mineList = new ArrayList<MineTile>();

        mineList.add(board[2][2]);
        mineList.add(board[2][3]);
        mineList.add(board[5][6]);
        mineList.add(board[3][4]);
        mineList.add(board[1][1]);
    }

    public void revealMines() {
        for (int i = 0; i < mineList.size(); i++) {
            MineTile tile = mineList.get(i);
            tile.setText("💣");
        }
    }

    // public void paintComponent (Graphics g) {
    //   super.paintComponent(g);
    //   draw(g);
    // }

    // public void draw(Graphics g) {
    //   // DRAW BACKGROUND
    //   if (backgroundImg != null) {
    //       g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, this);
    //   }
        
    //   g.setColor(Color.black);
    //   g.setFont(new Font("Arial", Font.PLAIN, 32));
    //   g.drawString("Choose your difficulty: ", 10, 35);
    // }

    // @Override
    // public void actionPerformed(ActionEvent e) {
    //   repaint();
    // }

}
