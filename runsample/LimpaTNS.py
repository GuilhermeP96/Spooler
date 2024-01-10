# -*- coding: ISO-8859-1 -*-
import os

def format_tnsnames(file_path):
    """
    Formata o conteúdo do arquivo TNSNAMES.ora em linhas únicas para cada entrada.

    Args:
    file_path (str): Caminho para o arquivo TNSNAMES.ora.

    Returns:
    list: Lista de strings formatadas do arquivo TNSNAMES.ora.
    """
    with open(file_path, 'r') as file:
        lines = file.readlines()

    formatted_lines = []
    current_entry = []
    inside_entry = False

    for line in lines:
        stripped_line = line.strip()

        if not stripped_line.startswith('#') and stripped_line != '':
            inside_entry = True
            current_entry.append(stripped_line.replace('\n', ' '))
        else:
            if inside_entry:
                formatted_lines.append(' '.join(current_entry) + '\n')
                current_entry = []
                inside_entry = False
            if stripped_line.startswith('#'):
                formatted_lines.append(line)

    if inside_entry:
        formatted_lines.append(' '.join(current_entry) + '\n')

    return formatted_lines

# Exemplo de uso
tns_admin_path = os.getenv('TNS_ADMIN')
if tns_admin_path:
    expanded_tns_admin_path = os.path.expandvars(tns_admin_path)
    file_path = os.path.join(expanded_tns_admin_path, 'TNSNAMES.ora')
    try:
        formatted_content = format_tnsnames(file_path)

        # Define o caminho do arquivo formatado no mesmo diretório que TNS_ADMIN
        formatted_file_path = os.path.join(expanded_tns_admin_path, 'TNSNAMES_formatado.ora')

        # Escrever o conteúdo formatado no arquivo formatado
        with open(formatted_file_path, 'w') as file:
            file.writelines(formatted_content)
        print(f"Arquivo gerado com sucesso: {formatted_file_path}")
    except FileNotFoundError:
        print(f"Arquivo não encontrado: {file_path}")
else:
    print("Variável de ambiente TNS_ADMIN não definida.")