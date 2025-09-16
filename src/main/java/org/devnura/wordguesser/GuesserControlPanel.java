package org.devnura.wordguesser;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class GuesserControlPanel {

    private JPanel mainPanel;
    private JPanel gridPanel;
    private JPanel keyboardPanel;
    private final JTextField[][] grid;
    private final int rows;
    private final int cols;
    private final WordGuesser wordGuesser;
    private final JButton[] letterButtons = new JButton[26];

    public GuesserControlPanel(JTextField[][] grid, int rows, int cols, WordGuesser wordGuesser) {
        this.grid = grid;
        this.rows = rows;
        this.cols = cols;
        this.wordGuesser = wordGuesser;
        initializePanel();
    }

    private void initializePanel() {
        mainPanel = new JPanel(new BorderLayout(10, 10));
        gridPanel = new JPanel(new GridLayout(rows, cols, 5, 5));
        gridPanel.setPreferredSize(new Dimension(400, 350));
        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                grid[i][j] = new JTextField();
                grid[i][j].setHorizontalAlignment(JTextField.CENTER);
                grid[i][j].setFont(new Font("Arial", Font.BOLD, 20));
                grid[i][j].setEditable(i == 0);
                int row = i;
                grid[i][j].addKeyListener(new KeyAdapter() {
                    @Override
                    public void keyTyped(KeyEvent e) {
                        JTextField source = (JTextField) e.getSource();
                        if (!wordGuesser.getPlaying() || row != wordGuesser.getCurrentRow()) {
                            e.consume();
                            return;
                        }
                        char typedChar = e.getKeyChar();
                        if (!Character.isLetter(typedChar)) {
                            e.consume();
                            return;
                        }
                        if (source.getText().length() == 1) {
                            e.consume();
                        } else {
                            source.setText(String.valueOf(typedChar).toUpperCase());
                            wordGuesser.moveToNextCell();
                            e.consume();
                        }
                    }

                    @Override
                    public void keyPressed(KeyEvent e) {
                        JTextField source = (JTextField) e.getSource();
                        if (!wordGuesser.getPlaying() || row != wordGuesser.getCurrentRow()) {
                            e.consume();
                            return;
                        }
                        if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                            wordGuesser.handleGuess();
                            e.consume();
                        } else if (e.getKeyCode() == KeyEvent.VK_BACK_SPACE) {
                            if (!source.getText().isEmpty()) {
                                source.setText("");
                            } else {
                                wordGuesser.moveToPreviousCell();
                            }
                            e.consume();
                        }
                    }
                });
                gridPanel.add(grid[i][j]);
            }
        }

        initializeKeyboard();
        mainPanel.add(gridPanel, BorderLayout.CENTER);
        mainPanel.add(keyboardPanel, BorderLayout.SOUTH);
    }

    private void initializeKeyboard() {
        keyboardPanel = new JPanel();
        keyboardPanel.setLayout(new BoxLayout(keyboardPanel, BoxLayout.Y_AXIS));
        JPanel row1 = new JPanel(new GridLayout(1, 10, 5, 5));
        JPanel row2 = new JPanel(new GridLayout(1, 9, 5, 5));
        JPanel row3 = new JPanel(new GridLayout(1, 8, 5, 5));

        for (int i = 0; i < 26; i++) {
            JButton button = new JButton(String.valueOf((char) ('A' + i)));
            button.setEnabled(false);
            button.setFont(new Font("Arial", Font.BOLD, 14));
            button.setForeground(Color.BLACK);
            button.setOpaque(true);
            letterButtons[i] = button;

            if (i < 10) {
                row1.add(button);
            } else if (i < 19) {
                row2.add(button);
            } else {
                row3.add(button);
            }
        }

        int emptyCells = 10 - 8;
        for (int i = 0; i < emptyCells / 2; i++) {
            row3.add(new JPanel());
        }

        keyboardPanel.add(row1);
        keyboardPanel.add(Box.createVerticalStrut(5));
        keyboardPanel.add(row2);
        keyboardPanel.add(Box.createVerticalStrut(5));
        keyboardPanel.add(row3);
    }

    public void updateKeyboard(char letter, Color color) {
        JButton button = letterButtons[Character.toUpperCase(letter) - 'A'];
        button.setBackground(color);
        button.setForeground(Color.BLACK);
        button.setOpaque(true);
    }

    public JPanel getMainPanel() {
        return mainPanel;
    }
}
