# excel_writer.py

import pandas as pd
import os
from config import EXCEL_PATH

def write_to_excel(data):
    if os.path.exists(EXCEL_PATH):
        df = pd.read_excel(EXCEL_PATH)
    else:
        df = pd.DataFrame(columns=['Nome', 'CPF', 'RG'])

    df.loc[len(df)] = [data['nome'], data['cpf'], data['rg']]
    df.to_excel(EXCEL_PATH, index=False)
