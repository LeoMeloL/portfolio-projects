# 🌲 Procedural 3D Forest Generator

A real-time 3D procedural **forest simulation** built in JavaScript using **WebGL**, with customizable parameters and user-controlled exploration. Trees, rocks, and plants are procedurally generated using vector-based logic and rendered with **vertex/fragment shaders (vs/fs)**.

## 🧠 Project Overview

This project demonstrates the use of **procedural generation** and **real-time rendering** in a 3D environment using only JavaScript and low-level WebGL shaders. The user can **move through the forest** and adjust a variety of environmental parameters.

## 🎮 Features

- 🏃 User-controlled first-person movement
- 🌳 Procedural placement of trees, rocks, and vegetation
- 📏 Customizable forest size and density
- 🌿 Real-time updates to:
  - Number of trees
  - Density of distribution
  - Number of rocks and plants
  - Forest boundaries
- 💡 Dynamic lighting and shading with **GLSL shaders**
- 🔄 Efficient rendering using buffer geometry and instancing

## 🧰 Technologies Used

- **JavaScript**
- **WebGL**
- **GLSL (Vertex & Fragment Shaders)**
- **Custom Vector and Matrix math**
- HTML5 & Canvas

## 🚀 How to Run

1. **Clone the repository**:
   ```bash
   git clone https://github.com/LeoMeloL/portfolio-projects/new/main/Computer-Graphics/Procedural-GeneratedForest.git
   cd Procedural-Generated-Forest

2. **Open index.html in a browser**:
  
You may need to use a local web server due to browser CORS policies.

  ```bash
  # With Python 3
  python -m http.server
  ```
3. **Use the UI panel to**:

Adjust forest size

Change tree density

Modify rock/plant count

Toggle rendering settings

## 🕹 Controls
W / A / S / D – Move forward, left, back, right

Mouse – Look around

UI Panel – Modify world generation settings

## 👨‍💻 Author 
Developed by Leonardo Melo as part of a computer graphics course
