package org.devnura.wordguesser;

import java.awt.Color;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JFrame;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.JOptionPane;

public class WordGuesser {

    private static final List<String> wordList = new ArrayList<>();
    private String selectedWord;
    private GuesserControlPanel controlPanel;
    private JTextField[][] grid;
    private JFrame frame;
    private final int cols = 5;
    private final int rows = 6;
    private int currentRow = 0;
    private int currentColumn = 0;
    private boolean playing = true;

    public WordGuesser() {
        storeWords();
        selectWord();
        setFrame();
    }

    private static void storeWords() {
        File wordFile = new File("lib/words.txt");
        if (!wordFile.exists()) {
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(wordFile))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (!line.isBlank()) {
                    wordList.add(line.trim().toLowerCase());
                }
            }
        } catch (IOException ignored) {
        }
    }

    private void selectWord() {
        if (wordList.isEmpty()) {
            throw new IllegalStateException("Word list is empty.");
        }
        int randomIndex = (int) (Math.random() * wordList.size());
        selectedWord = wordList.get(randomIndex);
        System.out.print(selectedWord);
    }

    private void setFrame() {
        frame = new JFrame("Word Guesser");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 500);
        grid = new JTextField[rows][cols];
        controlPanel = new GuesserControlPanel(grid, rows, cols, this);
        frame.add(controlPanel.getMainPanel());
        frame.setVisible(true);
        grid[0][0].requestFocus();
    }

    public void handleGuess() {
        if (currentRow >= rows || !playing) {
            return;
        }
        StringBuilder guessBuilder = new StringBuilder();
        for (int i = 0; i < cols; i++) {
            guessBuilder.append(grid[currentRow][i].getText().toLowerCase());
        }
        String guess = guessBuilder.toString();
        if (guess.length() != cols || !wordList.contains(guess)) {
            JOptionPane.showMessageDialog(frame, "Invalid word!");
            return;
        }
        for (int i = 0; i < cols; i++) {
            grid[currentRow][i].setEditable(false);
        }
        char[] wordChars = selectedWord.toCharArray();
        char[] guessChars = guess.toCharArray();
        int[] wordCharCounts = new int[26];
        for (char c : wordChars) {
            wordCharCounts[c - 'a']++;
        }
        for (int i = 0; i < cols; i++) {
            if (guessChars[i] == wordChars[i]) {
                grid[currentRow][i].setBackground(Color.GREEN);
                controlPanel.updateKeyboard(guessChars[i], Color.GREEN);
                wordCharCounts[guessChars[i] - 'a']--;
            }
        }
        for (int i = 0; i < cols; i++) {
            if (grid[currentRow][i].getBackground() != Color.GREEN) {
                if (wordCharCounts[guessChars[i] - 'a'] > 0) {
                    grid[currentRow][i].setBackground(Color.YELLOW);
                    controlPanel.updateKeyboard(guessChars[i], Color.YELLOW);
                    wordCharCounts[guessChars[i] - 'a']--;
                } else {
                    grid[currentRow][i].setBackground(Color.RED);
                    controlPanel.updateKeyboard(guessChars[i], Color.RED);
                }
            }
        }
        if (guess.equals(selectedWord)) {
            playing = false;
            JOptionPane.showMessageDialog(frame, "You guessed correctly!");
            return;
        }
        currentRow++;
        currentColumn = 0;
        if (currentRow >= rows) {
            playing = false;
            JOptionPane.showMessageDialog(frame, "You lost! The word was: " + selectedWord);
            return;
        }
        for (int i = 0; i < cols; i++) {
            grid[currentRow][i].setEditable(true);
            grid[currentRow][i].setBackground(Color.WHITE);
        }
        grid[currentRow][0].requestFocus();
    }

    public void moveToNextCell() {
        if (currentColumn < cols - 1) {
            currentColumn++;
        }
        grid[currentRow][currentColumn].requestFocus();
    }

    public void moveToPreviousCell() {
        if (currentColumn > 0) {
            currentColumn--;
        }
        grid[currentRow][currentColumn].setText("");
        grid[currentRow][currentColumn].requestFocus();
    }

    public int getCurrentRow() {
        return currentRow;
    }

    public boolean getPlaying() {
        return playing;
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(WordGuesser::new);
    }
}
