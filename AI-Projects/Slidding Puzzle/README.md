# 🧩 Sliding Puzzle Solver – A* and Search Algorithms (Python)

This project implements a **Sliding Puzzle (8-puzzle or 15-puzzle)** solver using various search algorithms, including the **A\*** algorithm with **Manhattan distance heuristic**, as well as uninformed search methods like **Breadth-First Search**, **Depth-Limited Search**, and **Iterative Deepening DFS**.

---

## 📌 Project Overview

Sliding Puzzle is a classic tile-based game where the objective is to move numbered tiles into the correct order by sliding them into the empty space. This project focuses on solving the puzzle programmatically using **informed and uninformed search algorithms**.

---

## 🤖 Implemented Algorithms

### ✅ Informed Search

- **A\* (A-Star)**:
  - Heuristic: **Manhattan Distance** (sum of distances of each tile from its goal position)
  - Guarantees an optimal solution if the heuristic is admissible
  - Prioritizes paths with lowest estimated total cost (`f(n) = g(n) + h(n)`)

### 🔍 Uninformed Search

- **Breadth-First Search (BFS)**
- **Depth-First Search (DFS)**
- **Depth-Limited Search (DLS)**
- **Iterative Deepening Depth-First Search (IDDFS)**

---

## 🧰 Technologies Used

- **Python 3.x**
- No external libraries required (uses only built-in modules: `heapq`, `collections`, etc.)

---
## 📊 Heuristic – Manhattan Distance
Each tile contributes to the heuristic by computing:

```scss
abs(current_row - goal_row) + abs(current_col - goal_col)
This guides A* to prefer tiles closer to their goal positions.
```

##👨‍💻 Author
Developed by Leonardo Melo as part of an artificial intelligence and algorithms study project.
