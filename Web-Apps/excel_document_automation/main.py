# main.py

#If you want to use it, you need your own credentials from google cloud console:
#Download client outh2 id, its a .json document
#Put it on the folder and rename it to client_secrets.json
#Also update config.py with your drive ID

import time
import os
from config import *
from drive_handler import authenticate, list_new_files, download_file, mark_file_as_processed
from document_processor import extract_text, parse_data
from excel_writer import write_to_excel

os.makedirs(TEMP_DOWNLOAD_PATH, exist_ok=True)

def main():
    drive = authenticate()

    while True:
        print("Verifying new documents...")
        new_files = list_new_files(drive, FOLDER_ID)
        for file in new_files:
            print(f"Processing: {file['title']}")
            local_path = download_file(file, TEMP_DOWNLOAD_PATH)
            texto = extract_text(local_path)
            dados = parse_data(texto)
            write_to_excel(dados)
            mark_file_as_processed(file['title'])
            print(f"Data saved: {dados}")
        time.sleep(CHECK_INTERVAL_SECONDS)

if __name__ == '__main__':
    main()
