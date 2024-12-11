from googleapiclient.discovery import build
from google_auth_oauthlib.flow import InstalledAppFlow
from google.auth.transport.requests import Request
from googleapiclient.http import MediaFileUpload
import os
import pickle
from datetime import datetime, timedelta

SCOPES = ['https://www.googleapis.com/auth/drive']

def authenticate():
    creds = None
    CLIENT_SECRET_FILE = ''
    TOKEN_FILE = 'token.pickle'

    if os.path.exists(TOKEN_FILE):
        with open(TOKEN_FILE, 'rb') as token:
            creds = pickle.load(token)

    if not creds or not creds.valid:
        if creds and creds.expired and creds.refresh_token:
            creds.refresh(Request())
        else:
            if not os.path.exists(CLIENT_SECRET_FILE):
                print(f"Erro: Arquivo {CLIENT_SECRET_FILE} não encontrado.")
                return None, None

            flow = InstalledAppFlow.from_client_secrets_file(
                CLIENT_SECRET_FILE, SCOPES)
            creds = flow.run_local_server(port=0)

        with open(TOKEN_FILE, 'wb') as token:
            pickle.dump(creds, token)

    service = build('drive', 'v3', credentials=creds)
    return creds, service

def list_files(service):
    results = service.files().list(
        pageSize=10, fields="nextPageToken, files(id, name)").execute()
    items = results.get('files', [])

    if not items:
        print('No files found.')
    else:
        print('Files:')
        for item in items:
            print(f"{item['name']} ({item['id']})")

def upload_file(service, file_path, file_name):
    file_metadata = {'name': file_name}
    media = MediaFileUpload(file_path, mimetype='text/plain')
    file = service.files().create(body=file_metadata, media_body=media, fields='id').execute()
    print(f'Arquivo {file.get("id")} carregado com sucesso.')

def drive_statistics(service):
    about = service.about().get(fields="storageQuota").execute()
    print(f"Espaço total: {about['storageQuota']['limit']}, Usado: {about['storageQuota']['usage']}")

#Use with caution
def delete_file(service, file_id):
    service.files().delete(fileId=file_id).execute()
    print(f'Arquivo {file_id} excluído com sucesso.')

def show_menu():
    print("\nEscolha uma opção:")
    print("1. Listar arquivos")
    print("2. Fazer upload de um arquivo")
    print("3. Ver estatísticas do Google Drive")
    print("4. Excluir um arquivo")
    print("5. Ver arquivos modificados recentemente")
    print("6. Sair")

def list_recent_changes(service, days=7):
    time_limit = (datetime.now() - timedelta(days=days)).isoformat() + 'Z'

    results = service.files().list(
        q=f"modifiedTime > '{time_limit}'",
        pageSize=10, 
        fields="nextPageToken, files(id, name, modifiedTime)"
    ).execute()

    items = results.get('files', [])

    if not items:
        print(f'Nenhum arquivo modificado nos últimos {days} dias.')
    else:
        print(f'Arquivos modificados nos últimos {days} dias:')
        for item in items:
            print(f"{item['name']} ({item['id']}) - Modificado em: {item['modifiedTime']}")

def main():
    creds, service = authenticate()
    if service is None:
        print("Erro na autenticação. Encerrando o programa.")
        return

    while True:
        show_menu()
        choice = input("Digite sua escolha (1-6): ")

        if choice == '1':
            list_files(service)
        elif choice == '2':
            file_path = input("Digite o caminho do arquivo para upload: ")
            file_name = input("Digite o nome do arquivo no Google Drive: ")
            upload_file(service, file_path, file_name)
        elif choice == '3':
            drive_statistics(service)
        elif choice == '4':
            file_id = input("Digite o ID do arquivo a ser excluído: ")
            delete_file(service, file_id)
        elif choice == '5':
            days = int(input("Digite o número de dias para verificar modificações recentes: "))
            list_recent_changes(service, days)
        elif choice == '6':
            print("Saindo...")
            break
        else:
            print("Opção inválida. Tente novamente.")


if __name__ == '__main__':
    main()
