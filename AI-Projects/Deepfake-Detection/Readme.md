# 🧠 Deepfake Detection: A Comparative Study of CNNs, Transfer Learning, and Vision Transformers

This project is part of my Computer Science graduation thesis and focuses on analyzing and comparing different neural network architectures for the task of **deepfake detection**.  
It was developed entirely in a **Jupyter Notebook (Google Colab)** environment and extensively documented, explaining the reasoning and results of each experiment.

---

## 📘 Overview

The main goal of this project is to study how different model architectures perform when distinguishing **AI-generated (fake)** images from **real** ones.  
The experiments include:

- **Baseline CNN models** — Simple convolutional architectures built from scratch.
- **Transfer Learning with Xception** — Using a pre-trained Xception network to detect local facial artifacts.
- **Vision Transformer (ViT)** — Exploring transformer-based architectures for global artifact detection.
- **Hybrid Model (ViT + Xception)** — A custom model combining the strengths of ViTs (global artifacts) and CNNs (local artifacts).

Several variations and optimizations were tested during development, but only the **main representative models** are included for clarity and reproducibility.

---

## ⚙️ How to Run

The notebook requires access to a **Google Drive** containing a **deepfake dataset**.  
Due to security and privacy reasons, the dataset used in this study **will not be publicly shared**.

To run the project:

1. Open the notebook in Google Colab.  
2. Mount your Drive containing a deepfake dataset.  
3. Follow the notebook cells in order — each section is clearly explained.

---

## 🧩 Project Structure

DeepfakeDetection/

│

├── DeepfakeDetection.ipynb # Main Jupyter Notebook

├── README.md # Project documentation

└── requirements.txt # Dependencies


---

## 🧠 Technologies Used

- Python 3.x  
- TensorFlow / Keras  
- NumPy / Pandas / Matplotlib  
- OpenCV   
- Vision Transformers (ViT)  

---

## 📈 Experimental Setup

Each architecture was trained and evaluated using the same dataset split to ensure fair comparison.  
Metrics such as **accuracy**, **F1-score**, **AUC** and **confusion matrices** were used to assess model performance.

---

## 🔬 Results Summary

| Model Type | Description | Main Strength | Performance |
|-------------|--------------|----------------|--------------|
| **Baseline CNN** | Simple custom-built CNN | Fast, low complexity | Moderate |
| **Xception (Transfer Learning)** | Pretrained CNN for local features | Excellent for fine-grained facial cues | High |
| **Vision Transformer (ViT)** | Transformer-based global feature extractor | Strong at detecting global inconsistencies | High |
| **Hybrid (ViT + Xception)** | Combines CNN and ViT components | Captures both local and global artifacts | **Best overall** |

---

## ⚠️ Dataset Notice

The dataset used in this research contains real and AI-generated facial images.  
Due to ethical and security reasons, it will **not be shared publicly**.  
However, the notebook is compatible with most public deepfake datasets (e.g., FaceForensics++, DeepFake Detection Challenge dataset, etc.) — paths may need minor adjustments.

---

## 🧾 License

This repository is intended for **academic and educational purposes**.  
If you wish to use or reference this work, please include proper attribution.

---

## 👤 Author

**Leonardo Melo**  
Bachelor’s Degree in Computer Science  
[GitHub](https://github.com/LeoMeloL) 

---
