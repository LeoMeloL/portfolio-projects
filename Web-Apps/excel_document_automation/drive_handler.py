# drive_handler.py

from pydrive.auth import GoogleAuth
from pydrive.drive import GoogleDrive
import os
from config import TEMP_DOWNLOAD_PATH, PROCESSED_FILES_DB
import json

def authenticate():
    gauth = GoogleAuth()
    gauth.LocalWebserverAuth()
    return GoogleDrive(gauth)

def list_new_files(drive, folder_id):
    file_list = drive.ListFile({'q': f"'{folder_id}' in parents and trashed=false"}).GetList()
    processed = load_processed_files()
    new_files = [f for f in file_list if f['title'] not in processed]
    return new_files

def download_file(file, path):
    file_path = os.path.join(path, file['title'])
    file.GetContentFile(file_path)
    return file_path

def mark_file_as_processed(filename):
    processed = load_processed_files()
    processed.append(filename)
    with open(PROCESSED_FILES_DB, 'w') as f:
        json.dump(processed, f)

def load_processed_files():
    if not os.path.exists(PROCESSED_FILES_DB):
        return []
    try:
        with open(PROCESSED_FILES_DB, 'r') as f:
            return json.load(f)
    except json.JSONDecodeError:
        return []
