from tracemalloc import BaseFilter
import pyautogui
from PIL import Image, ImageOps, ImageEnhance, ImageFilter
import pytesseract
import numpy as np

def preprocess_image(image):
    gray_image = image.convert('L')

    enhancer = ImageEnhance.Contrast(gray_image)
    enhanced_image = enhancer.enhance(2)  
  
    threshold = 128  
    binarized_image = enhanced_image.point(lambda p: p > threshold and 255)

 
    binarized_image = binarized_image.filter(ImageFilter.MedianFilter(size=3))

    return binarized_image

screenshot = pyautogui.screenshot()
char_box = (1536, 100, 1920, 748)
charPrint = screenshot.crop(char_box)

processed_image = preprocess_image(charPrint)

processed_image.save("processed_char.png")

charText = pytesseract.image_to_string(processed_image, lang='eng').strip()
print(f"Texto extraído da área dos personagens: {charText}")
