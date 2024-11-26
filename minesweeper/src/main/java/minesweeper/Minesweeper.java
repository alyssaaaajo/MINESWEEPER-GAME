package minesweeper;

import java.awt.*;
import java.awt.event.*;
import java.nio.channels.Pipe;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.*;

public class Minesweeper extends JPanel implements ActionListener {
  int boardWidth = 1200;
  int boardHeight = 600;

  Image backgroundImg;

  public Minesweeper() {
    setPreferredSize(new Dimension(boardWidth, boardHeight));
    setFocusable(true);

    backgroundImg = new ImageIcon(getClass().getResource("./ocean.gif")).getImage();
  }

  public void paintComponent (Graphics g) {
    super.paintComponent(g);
    draw(g);
  }

  public void draw(Graphics g) {
    // DRAW BACKGROUND
    if (backgroundImg != null) {
        g.drawImage(backgroundImg, 0, 0, boardWidth, boardHeight, this);
    }
    
    g.setColor(Color.black);
    g.setFont(new Font("Arial", Font.PLAIN, 32));
    g.drawString("Choose your difficulty: ", 10, 35);
  }

  @Override
  public void actionPerformed(ActionEvent e) {
    repaint();
  }

}
