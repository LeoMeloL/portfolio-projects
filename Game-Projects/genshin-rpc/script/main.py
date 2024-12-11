import pyautogui
from pypresence import Presence
import time
from PIL import Image, ImageOps, ImageEnhance
import pytesseract
import re
import pygetwindow as gw
from pynput import keyboard
from pynput.keyboard import Listener
from difflib import SequenceMatcher

pytesseract.pytesseract.tesseract_cmd = r'C:\\Program Files\\Tesseract-OCR\\tesseract.exe'
CLIENT_ID = "hidden"

rpc = Presence(CLIENT_ID)
rpc.connect()

lvl_box = (735, 980, 1185, 1080)
char_box1 = (1536, 200, 1920, 328)
char_box2 = (1536, 300, 1920, 408)
char_box3 = (1536, 400, 1920, 488)
char_box4 = (1536, 500, 1920, 568)
region_box = (1350, 930, 1920, 1030)
region = None
subregion = None
window_name = "Genshin Impact"

personagens = ["Neuvillette", "Zhongli", "Mualani", "Albedo", "Ganyu", "Klee", "Xiao", "Mona", "Raiden Shogun", "Kazuha"]

detected_characters = []
active_character = None
pressed_keys = set()
character_image = None

print("Starting level and character detection...")

def detect_char(screenshot):
    char_boxes = [char_box1, char_box2, char_box3, char_box4]
    detected = []
    
    for char_box in char_boxes:
        charPrint = screenshot.crop(char_box)
        charPrintClean = charPrint.resize((charPrint.width * 2, charPrint.height * 2))
        charPrintClean = charPrint.convert("L")
        charPrintClean = ImageEnhance.Contrast(charPrintClean).enhance(1.0) 
        charPrintClean = ImageEnhance.Sharpness(charPrintClean).enhance(1.0)
        
        char_text = pytesseract.image_to_string(charPrintClean, lang='eng').strip()
        
        for nome in personagens:
            similarity = SequenceMatcher(None, nome.lower(), char_text.lower()).ratio()
            if similarity > 0.3:  
                detected.append(nome)
                break 
    
    return detected

def find_region(screenshot):
    region = screenshot.crop(region_box)
    regionClean = region.resize((region.width * 2, region.height * 2))
    regionClean = region.convert("L")
    regionClean = ImageEnhance.Contrast(regionClean).enhance(1.0)
    regionClean = ImageEnhance.Sharpness(regionClean).enhance(1.0)

    region_text = pytesseract.image_to_string(regionClean, lang='eng').strip()
    
    regions = {
        "Natlan": ["Bacia das chamas incontaveis", "Montanha Coatepec", "Vale Tequemecan", "Pantanal Toyac"],
        "Desert": ["Deserto de Hadramaveth", "Hipostilo desertico", "Baixo setekh", "Alto setekh", "Sementeira Perdida", "Gavireh lajavard", "Reino de Farakhkert"],
        "Sumeru": ["Reino ashavan", "Vale ardravi", "Floresta avidya", "Campo vissudha", "Vanarana", "Floresta lokapala"],
        "Liyue": ["Lisha", "Despenhadeiro", "Minlin", "Estuario de qiongji", "Planicies Bishu", "Mar de nuvens"],
        "Monstadt": ["Terras das altas lamentacoes", "Colina silvante", "Vales das estrelas Cadentes", "Montanha das Coroas", "Espinha do dragao"],
        "Fontaine": ["Distrito belleau", "Distrito beryl", "Regiao mortaine", "Distrito da corte de fontaine", "Floresta de erinnyes", "regiao liffey", "Instituto de pesquisa de engenharia de energia cinetica de fontaine", "Regiao nostoi"],
        "Chenyu": ["Vale chenyu: vale de cima", "Vale chenyu: Montanha ao sul", "Monte Laixin"],
        "Inazuma": ["Ilha narukumi", "Kannazuka", "Ilha yashiori", "Ilha watatsumi", "Ilha seirai", "Ilha tsurumi"]
    }

    region_text = region_text.lower()

    similarity_threshold = 0.3
    for main_region, subareas in regions.items():
        for subarea in subareas:
            similarity = SequenceMatcher(None, subarea.lower(), region_text).ratio()
            if similarity > similarity_threshold:
                print(main_region, subarea)
                return main_region, subarea

    return None, None 



def on_press(key):
    global active_character, region, subregion
    try:
        if hasattr(key, 'char') and key.char not in pressed_keys:
            pressed_keys.add(key.char) 

            if key.char == '1':
                active_character = detected_characters[0]
                print(active_character)
                character_image = "neuvillete_icon"
            elif key.char == '2':
                active_character = detected_characters[1]
                print(active_character)
                character_image = "mualani_icon"
            elif key.char == '3':
                active_character = detected_characters[2]
                print(active_character)
                character_image = "neuvillete_icon"
            elif key.char == '4':
                active_character = detected_characters[3]
                print(active_character)
                character_image = "zhongli_icon"
            elif key.char == 'm':
                time.sleep(2)
                screenshot = pyautogui.screenshot()
                region, subregion = find_region(screenshot)
                
                if region and subregion: 
                    print(f"Exploring {subregion}, {region}")
                    rpc.update(
                        state=f"Exploring {region}",
                        details=f"Playing as {active_character}",
                        large_image="natlan_icon",
                        large_text=f"Exploring {subregion}, {region}",
                        small_text=f"Playing as {active_character} Lvl {numero_nivel}",
                        start=time.time()
                    )
                else:
                    print("Região ou sub-região não detectada.")
                
                
    except Exception as e:
        print(f"Erro ao processar tecla: {e}")

def on_release(key):
    try:
        if hasattr(key, 'char') and key.char in pressed_keys:
            pressed_keys.remove(key.char)
    except Exception as e:
        print(f"Erro ao liberar tecla: {e}")

listener = Listener(on_press=on_press, on_release=on_release)
listener.start()

while True:
    try:
        active_window = gw.getActiveWindow()
        screenshot = pyautogui.screenshot()
        if active_window and active_window.title == window_name:
            if len(detected_characters) != 4:  
                screenshot = pyautogui.screenshot()
                detected_characters = detect_char(screenshot)
                if len(detected_characters) == 4:  
                    print(f"Personagens detectados: {detected_characters}")
                else:
                    print("Ainda não foram detectados 4 personagens. Tentando novamente...")
                    time.sleep(2)
                    continue  
            lvlPrint = screenshot.crop(lvl_box)
            lvlPrint.save("lvl.png")
            lvlText = pytesseract.image_to_string(lvlPrint, lang='eng').strip()
            match = re.search(r'Nv\.\d+', lvlText)
            
            if match:
                nivel = match.group()
                numero_nivel = int(nivel.split('.')[1]) 
                print(f"Nível detectado: {numero_nivel}")
                print(detected_characters)
                
                if not active_character:
                    active_character = detected_characters[0] if detected_characters else "Neuvillete"
                    character_image = "neuvillete_icon"

                print(active_character)
                
                if not region:
                    region = "Natlan"
                
                rpc.update(
                    state=f"Exploring Natlan",
                    details=f"Playing {active_character}",
                    large_image="natlan_icon",
                    large_text="Exploring {subregion}, {region}",
                    small_image=character_image,
                    small_text=f"Playing as {active_character} Lvl {numero_nivel}",
                    start=time.time()
                )

            else:
                print("Nível não encontrado no texto extraído.")

            time.sleep(2)

    except Exception as e:
        print(f"Ocorreu um erro: {e}")
        time.sleep(5)
