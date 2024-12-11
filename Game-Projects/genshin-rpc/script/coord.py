import pydirectinput
import time

window_name = "Genshin Impact"

while True:
    x, y = pydirectinput.position()
    print(f"Posição do mouse: x={x}, y={y}", end="\r")
    time.sleep(1)
    
width, height = screenshot.size
rect_width = int(width * 0.2)
rect_height = int(height * 0.6)  
left = width - rect_width
top = 100 
right = width 
bottom = rect_height  + 100