# document_processor.py

import pytesseract
from PIL import Image
from pdf2image import convert_from_path
import re
import os

def extract_text(file_path):
    if file_path.lower().endswith('.pdf'):
        images = convert_from_path(file_path)
        text = ""
        for img in images:
            text += pytesseract.image_to_string(img)
    else:
        img = Image.open(file_path)
        text = pytesseract.image_to_string(img)
    return text

def parse_data(text):
    cpf = re.search(r'\d{3}\.\d{3}\.\d{3}-\d{2}', text)
    rg = re.search(r'\d{2}\.\d{3}\.\d{3}-\d{1}', text)
    nome = ""

    linhas = text.split("\n")
    pegar_proximo_nome = False
    
    for linha in linhas:
        linha = linha.strip()

        # Nome
        if pegar_proximo_nome:
            if linha:
                nome = linha
                pegar_proximo_nome = False
        if "nome" in linha.lower():
            pegar_proximo_nome = True
            
    return {
        'nome': nome or 'NÃO ENCONTRADO',
        'cpf': cpf.group(0) if cpf else 'NÃO ENCONTRADO',
        'rg': rg.group(0) if rg else 'NÃO ENCONTRADO'
    }

def extract_nome(texto):
    linhas = texto.splitlines()
    for i, linha in enumerate(linhas):
        if "nome civil" in linha.lower():
            # A próxima linha é o nome verdadeiro
            if i + 1 < len(linhas):
                nome = linhas[i + 1].strip()
                if nome and not nome.lower().startswith("data de nascimento"):
                    return nome
    return "NOME_NAO_ENCONTRADO"
