# Spooler Project

O projeto Spooler � uma ferramenta de linha de comando para a execu��o eficiente de spools de dados do Oracle Database, otimizando consultas e a gera��o de relat�rios CSV.

## Pr�-requisitos

- Python: Para formata��o do TNSNAMES.ora;
- Java 8: Para execu��o da aplica��o.

### Para desenvolvedores

- ojdbc8

## Configura��o

### Vari�veis de Ambiente

Antes de executar a aplica��o Spooler, configure as seguintes vari�veis de ambiente na conta do usu�rio que ir� executar a aplica��o:

- `USUARIO_XE`: Define o usu�rio do Oracle Database. Por exemplo, `root`.
- `XE`: Define a senha do usu�rio do Oracle Database. Por exemplo, `toor`.

- Outras vari�veis podem ser criadas com aliases e valores de acordo com a quantidade de usu�rios e senhas necess�rias para diversos arquivos de configura��es para inst�ncias diferentes de bases de dados.

Isso garante que apenas a conta autenticada tenha acesso �s credenciais do banco de dados.

### TNSNAMES.ora

A aplica��o depende da correta localiza��o e formata��o do arquivo `TNSNAMES.ora`. Configure a vari�vel de ambiente `TNS_ADMIN` para apontar para o diret�rio que cont�m o arquivo `TNSNAMES.ora`.

### Limpeza do TNSNAMES.ora

Use o script `LimpaTNS.py` no diret�rio `runsample` para formatar o `TNSNAMES.ora` antes de usar o Spooler. Execute o script com o seguinte comando:

```bash
python LimpaTNS.py.
```

### Defini��o do NLS_LANG
A vari�vel NLS_LANG � configurada pelo arquivo batch spooler.cmd para definir o conjunto de caracteres para a sess�o do Oracle. O arquivo spooler.cmd deve ser executado para iniciar a aplica��o.




### Uso
Para executar a aplica��o, use o arquivo de exemplo:

```bash
runsample/spooler.cmd
```

### Arquivos de Configura��o e Execu��o
Os arquivos de exemplo para configura��o e query SQL est�o localizados no diret�rio runsample. O arquivo de configura��o confige.txt deve ser preenchido com os detalhes de conex�o, par�metros de formata��o e caminho do arquivo de sa�da conforme o exemplo abaixo:

- O campo `DB_INSTANCE` � o nome da String presente no TNSNAMES.ora.

```bash
DB_USER_ENV=USUARIO_XE
DB_PASSWORD_ENV=XE
DB_INSTANCE=XE
SQL_FILE_PATH=C:\\project\\runsample\\spool.sql
ARQUIVO_SAIDA=C:\\project\\output\\spool.csv
DECIMAL_SEPARATOR=.
DATE_FORMAT=DD/MM/YYYY
ENCLOSURE_CHARACTER="
COLUMN_SEPARATOR=;
CHARSET=UTF-8
```

A query de exemplo query_example.sql deve ser escrita sem ponto e v�rgula no final para evitar erros de sintaxe.

## Documenta��o Adicional
Toda a documenta��o do c�digo-fonte est� dispon�vel nos coment�rios das classes e m�todos no projeto. As conven��es de codifica��o seguem as pr�ticas recomendadas e atualizadas de desenvolvimento Java.

## Desempenho
Consultas diretas a tabelas sem necessidade de processamento prolongado da base de dados demonstraram uma redu��o significativa no tempo de execu��o, de aproximadamente 2 horas e meia para 4 minutos.

## Contribui��o
Para contribuir com o projeto, por favor, envie um e-mail para guilherme.pinheiro@gp96.com.br.

## Licen�a

Este projeto � licenciado sob a MIT License - veja o arquivo LICENSE.md para mais detalhes.
