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
  int boardWidth = 1200;
  int boardHeight = 600;

  JFrame frame = new JFrame("Minesweeper");
  JLabel textLabel = new JLabel();
  JPanel textPanel = new JPanel();
  JPanel boardPanel = new JPanel();

  MineTile[][] board = new MineTile[numRows][numCols];

  // Exceptionms, abstraction, encapsulation, inheritance, polymorphism, interfaec

  Image backgroundImg;

  public Minesweeper() {
    frame.setVisible(true);
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

    boardPanel.setLayout(new GridLayout(numRows, numCols));
    // boardPanel.setBackground(Color.green);
    frame.add(boardPanel);

    for (int r = 0; r < numRows; r++) {
      for (int c = 0; c < numCols; c++) {
        MineTile tile = new MineTile(r, c);
        board[r][c] = tile;
      }
    }

    // setPreferredSize(new Dimension(boardWidth, boardHeight));
    // setFocusable(true);

    // backgroundImg = new ImageIcon(getClass().getResource("./ocean.gif")).getImage();
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
