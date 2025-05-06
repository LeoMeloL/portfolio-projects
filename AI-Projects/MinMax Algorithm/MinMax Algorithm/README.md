# ❌⭕ Tic-Tac-Toe AI – Minimax Algorithm (Java)

This project implements an **AI opponent** for the classic game of **Tic-Tac-Toe**, using the **Minimax algorithm** in Java. The AI plays optimally, ensuring it never loses and always makes the best possible move.

## 🧠 What is Minimax?

The **Minimax algorithm** is a recursive decision-making algorithm used in turn-based games. It simulates all possible future game states to choose the move that minimizes the opponent's chances of winning, assuming both players play optimally.

In Tic-Tac-Toe, Minimax explores the entire game tree due to its manageable size, making it perfect for this kind of project.

## 🎮 Features

- ✅ Play against an unbeatable AI
- 🧠 Optimal decision-making with full Minimax recursion
- 🔄 Supports human vs AI or AI vs AI modes
- 👁️ Simple console-based board display
- 🧪 Clean and modular code for experimentation

## 🧰 Technologies Used

- Java (JDK 8+)
- No external libraries required

## 🧠 How the AI Works
Evaluates all available moves.

For each move, recursively simulates the outcome.

Scores game states:

+10 for a win, -10 for a loss, 0 for draw

Chooses the move that maximizes its minimum guaranteed score.

The AI assumes the human player plays optimally.

##👨‍💻 Author
Developed by Leonardo Melo as part of an artificial intelligence project.
