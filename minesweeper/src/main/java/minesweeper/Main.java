package minesweeper;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        int boardWidth = 400;
        int boardHeight = 200;

        JFrame frame = new JFrame("Minesweeper");
        frame.setLocationRelativeTo(null);
        // frame.setVisible(true);
        frame.setSize(boardWidth, boardHeight);
        frame.setResizable(false);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        
        Minesweeper minesweeper = new Minesweeper();
        frame.add(minesweeper);
        frame.pack();
        minesweeper.requestFocus();
        frame.setVisible(true);
    }
}