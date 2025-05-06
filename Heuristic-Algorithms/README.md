# 🚚 Traveling Salesman Problem (TSP) – Heuristic Algorithms

This repository contains two heuristic algorithms to approximate solutions for the **Traveling Salesman Problem (TSP)**:

- 🔁 **Christofides Algorithm**
- 🌲 **MST Approximation (Minimum Spanning Tree)**

Both algorithms provide efficient, polynomial-time approaches for finding near-optimal solutions to the NP-hard TSP.

## 📌 What is the TSP?

The **Traveling Salesman Problem** is a classic optimization problem that asks:

> "Given a list of cities and the distances between each pair of cities, what is the shortest possible route that visits each city exactly once and returns to the origin city?"

Exact solutions become computationally expensive as the number of cities increases. Hence, heuristics like Christofides and MSTApproximate offer good trade-offs between performance and solution quality.

## ⚙️ Algorithms Included

### 1. 🧠 Christofides Algorithm

- Guarantees a solution within **1.5× the optimal cost** (for metric TSP).
- Steps:
  1. Build a **Minimum Spanning Tree (MST)**
  2. Find nodes with **odd degree** in the MST
  3. Find a **minimum-weight perfect matching** among them
  4. Combine MST and matching into a **multigraph**
  5. Find an **Eulerian tour** and convert it into a **Hamiltonian circuit**

### 2. 🌲 MST Approximate Algorithm

- Builds an MST and performs a **pre-order traversal** to approximate a TSP tour.
- Simpler and faster, with approximation factor of **2× the optimal** (for metric TSP).
- Steps:
  1. Build MST
  2. Traverse the MST using DFS
  3. Return to the starting city to complete the circuit
 
## 🛠 Dependencies
Python 3.8+

networkx

numpy

matplotlib (optional for plotting)

pandas

## 👨‍💻 Author
Developed by Leonardo Melo and Igor Dutra– Computer Science students.
