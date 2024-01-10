package com.spooler.main;

import com.spooler.config.ConfigurationManager;
import com.spooler.database.ConnectionManager;
import com.spooler.database.SpoolRunner;
import com.spooler.util.DateUtils;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.Path;
import java.sql.Connection;
public class Main {

    public static void main(String[] args) {
        if (args.length < 1) {
            System.out.println("Uso: java -jar spooler.jar <Caminho completo do arquivo de configuração>");
            return;
        }
        System.out.println(DateUtils.getCurrentTimestamp() + " - Arquivo de parâmetros encontrado, iniciando aplicação...");
    	System.out.println(); 
        String configFilePath = args[0];
        try {
        	System.out.println(DateUtils.getCurrentTimestamp() + " - Chamando ConfigurationManager...");
        	System.out.println(); 
            ConfigurationManager configManager = new ConfigurationManager(configFilePath);
            
            System.out.println(DateUtils.getCurrentTimestamp() + "Definindo valores em variáveis...");
            String dbUserEnvName = configManager.getProperty("DB_USER_ENV");
            String dbPasswordEnvName = configManager.getProperty("DB_PASSWORD_ENV");
            String dbUser = System.getenv(dbUserEnvName);
            String dbPassword = System.getenv(dbPasswordEnvName);
            String dbInstance = configManager.getProperty("DB_INSTANCE");
            String dbUrl = "jdbc:oracle:thin:@" + dbInstance;

            // Imprimir as variáveis de configuração para depuração
            System.out.println("DB User Env Name: " + dbUserEnvName);
            System.out.println("DB Password Env Name: " + dbPasswordEnvName);
            System.out.println("DB User: " + dbUser);
            System.out.println("DB Instance: " + dbInstance);
            System.out.println("DB URL: " + dbUrl);

            Path tnsPath = Paths.get(System.getenv("TNS_ADMIN"), "tnsnames.ora");
            if (!Files.exists(tnsPath)) {
                System.err.println("Não foi possível encontrar o arquivo tnsnames.ora em: " + tnsPath);
                return;
            }

            // Mais variáveis de configuração
            String sqlFilePath = configManager.getProperty("SQL_FILE_PATH");
            String arquivoSaida = configManager.getProperty("ARQUIVO_SAIDA");
            String decimalSeparator = configManager.getProperty("DECIMAL_SEPARATOR");
            String dateFormat = configManager.getProperty("DATE_FORMAT");
            String enclosureCharacter = configManager.getProperty("ENCLOSURE_CHARACTER");
            String columnSeparator = configManager.getProperty("COLUMN_SEPARATOR");
            String charset = configManager.getProperty("CHARSET");

            // Imprimir o restante das variáveis de configuração para depuração
            System.out.println("SQL File Path: " + sqlFilePath);
            System.out.println("Arquivo Saida: " + arquivoSaida);
            System.out.println("Decimal Separator: " + decimalSeparator);
            System.out.println("Date Format: " + dateFormat);
            System.out.println("Enclosure Character: " + enclosureCharacter);
            System.out.println("Column Separator: " + columnSeparator);
            System.out.println("Charset: " + charset);
        	System.out.println(); 

            ConnectionManager connectionManager = new ConnectionManager(dbUrl, dbUser, dbPassword);
            try (Connection conn = connectionManager.openConnection()) {
                SpoolRunner spoolRunner = new SpoolRunner();
                spoolRunner.runSpool(sqlFilePath, conn, arquivoSaida, decimalSeparator, dateFormat, enclosureCharacter, columnSeparator, charset);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
