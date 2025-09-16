# WordGuesser

**WordGuesser** is a simple, fun word-guessing game inspired by *Wordle*. Guess the hidden word within six attempts and get real-time feedback on your guesses using color-coded hints:

- **Green**: Correct letter in the correct position.  
- **Yellow**: Correct letter but in the wrong position.  
- **Red**: Incorrect letter.  

## Features
- Interactive **6x5 grid** for entering guesses.  
- Automatic **cursor movement** as you type.  
- **Backspace support** to delete letters and move back.  
- **Locked rows**: previous guesses cannot be edited.  
- **Random word selection** from a `words.txt` file.  
- Visual **keyboard feedback** showing guessed letters and their correctness.  

## How to Play
1. Enter a **5-letter word** in the current row.  
2. Press **Enter** to submit your guess.  
3. Observe the **color-coded feedback** for each letter.  
4. If incorrect, type a new word in the **next row**.  
5. Win by guessing the word correctly within **6 attempts**, or see the correct word if you lose.  

## File Structure
```
WordGuesser/
│── GuesserControlPanel.java # Handles UI components (grid setup & key events)
│── WordGuesser.java # Manages game logic (word selection & guess evaluation)
│── lib/
│ └── words.txt # Word list for random selection
│── README.md # Project documentation
```

## Getting Started
1. Clone the repository.  
2. Ensure `words.txt` exists in the `lib/` folder.  
3. Compile and run `WordGuesser.java` using your preferred Java IDE
