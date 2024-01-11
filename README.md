# Spooler Project

O projeto Spooler é uma ferramenta de linha de comando para a execução eficiente de spools de dados do Oracle Database, otimizando consultas e a geração de relatórios CSV.

## Pré-requisitos

- Python: Para formatação do TNSNAMES.ora.
- Java 8: Para execução da aplicação.

### Para desenvolvedores

- ojdbc8.

## Configuração

### Variáveis de Ambiente

Antes de executar a aplicação Spooler, configure as seguintes variáveis de ambiente na conta do usuário que irá executar a aplicação:

- `USUARIO_XE`: Define o usuário do Oracle Database. Por exemplo, `root`.
- `XE`: Define a senha do usuário do Oracle Database. Por exemplo, `toor`.

- Outras variáveis podem ser criadas com aliases e valores de acordo com a quantidade de usuários e senhas necessárias para diversos arquivos de configurações para instâncias diferentes de bases de dados.

Isso garante que apenas a conta autenticada tenha acesso às credenciais do banco de dados.

### TNSNAMES.ora

A aplicação depende da correta localização e formatação do arquivo `TNSNAMES.ora`. Configure a variável de ambiente `TNS_ADMIN` para apontar para o diretório que contém o arquivo `TNSNAMES.ora`.

### Limpeza do TNSNAMES.ora

Use o script `LimpaTNS.py` no diretório `runsample` para formatar o `TNSNAMES.ora` antes de usar o Spooler. Execute o script com o seguinte comando:

```bash
python LimpaTNS.py
```

### Definição do NLS_LANG
A variável `NLS_LANG` é configurada temporariamente pelo arquivo batch `spooler.cmd` para definir o conjunto de caracteres para a sessão do Oracle. O arquivo `spooler.cmd` deve ser executado para iniciar a aplicação.

### Uso
Para executar a aplicação, personalize os arquivos `runsample/query.sql` e `runsample/config.txt`, após isso use o arquivo de exemplo `runsample/spooler.cmd` ou os comandos abaixo no Prompt de Comando do Windows:

```bash
set NLS_LANG=.AL32UTF8
java -jar runsample\Spooler.jar runsample\config.txt >> runsample\log.txt 2>&1
```

### Arquivos de Configuração e Execução
Os arquivos de exemplo para configuração e query SQL estão localizados no diretório `runsample`. O arquivo de configuração `config.txt` deve ser preenchido com os detalhes de conexão, parâmetros de formatação e caminho do arquivo de saída conforme o exemplo abaixo:

- O campo `DB_INSTANCE` é o nome da String presente no TNSNAMES.ora.
- Os formatos de casas decimais e datas são no padrão Java, e não Oracle.

A query de exemplo `query.sql` deve ser escrita sem ponto e vírgula no final para evitar erros de sintaxe.

## Documentação Adicional
Toda a documentação do código-fonte está disponível nos comentários das classes e métodos no projeto. As convenções de codificação seguem as práticas recomendadas e atualizadas de desenvolvimento Java.

O `spooler.cmd` está configurado para gerar um arquivo de despejo `log.txt` com os detalhes da execução.

## Desempenho
Consultas diretas a tabelas sem necessidade de processamento prolongado da base de dados demonstraram uma redução significativa no tempo de execução, de aproximadamente 2 horas e meia para 4 minutos.

## Contribuição
Para contribuir com o projeto, por favor, envie um e-mail para guilherme.pinheiro@gp96.com.br.

## Licença

Este projeto é licenciado sob a MIT License - veja o arquivo LICENSE.md para mais detalhes.

## Download

[Spooler Releases](https://github.com/GuilhermeP96/Spooler/releases)
