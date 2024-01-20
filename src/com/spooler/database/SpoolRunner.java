package com.spooler.database;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.OutputStreamWriter;
import java.io.FileOutputStream;
import java.io.PrintWriter;
import java.nio.charset.Charset;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.SimpleDateFormat;

import com.spooler.util.DateUtils;

public class SpoolRunner {

    public void runSpool(String sqlFilePath, Connection conn, String arquivoSaida, String decimalFormat, String decimalSeparator, String dateFormat, String enclosureCharacter, String columnSeparator, String charset) throws Exception {
        // Valores padrão do Oracle
        String defaultDecimalFormat = "#0.##";
        String defaultDecimalSeparator = ".";
        String defaultDateFormat = "yyyy-MM-dd";
        String defaultEnclosureCharacter = "";
        String defaultColumnSeparator = ",";
        String defaultCharset = "UTF-8";
               
        // Verificar parâmetros nulos e usar valores padrão, se necessário
        decimalFormat = (decimalFormat != null) ? decimalFormat : defaultDecimalFormat;
        decimalSeparator = (decimalSeparator != null) ? decimalSeparator : defaultDecimalSeparator;
        dateFormat = (dateFormat != null) ? dateFormat : defaultDateFormat;
        enclosureCharacter = (enclosureCharacter != null) ? enclosureCharacter : defaultEnclosureCharacter;
        columnSeparator = (columnSeparator != null) ? columnSeparator : defaultColumnSeparator;
        charset = (charset != null) ? charset : defaultCharset;

        // Imprimir avisos para parâmetros nulos
        if(decimalFormat.equals(null)) System.out.println("Aviso: Separador decimal nulo. Usando padrão: '" + defaultDecimalFormat + "'");
        if(decimalSeparator.equals(null)) System.out.println("Aviso: Separador decimal nulo. Usando padrão: '" + defaultDecimalSeparator + "'");
        if(dateFormat.equals(null)) System.out.println("Aviso: Formato de data nulo. Usando padrão: '" + defaultDateFormat + "'");
        if(enclosureCharacter.equals(null)) System.out.println("Aviso: Caractere enclausurador nulo. Usando padrão: '" + defaultEnclosureCharacter + "'");
        if(columnSeparator.equals(null)) System.out.println("Aviso: Separador de colunas nulo. Usando padrão: '" + defaultColumnSeparator + "'");
        if(charset.equals(null)) System.out.println("Aviso: Charset nulo. Usando padrão: '" + defaultCharset + "'");

    	System.out.println(DateUtils.getCurrentTimestamp() + " - Lendo arquivo SQL...");
    	System.out.println(); 
        try (BufferedReader br = new BufferedReader(new FileReader(sqlFilePath));
             PrintWriter pw = new PrintWriter(new OutputStreamWriter(new FileOutputStream(arquivoSaida), Charset.forName(charset)))) {
        	
            StringBuilder queryBuilder = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                // Ignora linhas em branco e comentários
                if (!line.trim().isEmpty() && !line.trim().startsWith("--")) {
                    queryBuilder.append(line).append(" ");
                }
            }
            
            String sqlQuery = queryBuilder.toString().trim();
            if (sqlQuery.isEmpty()) {
                throw new IllegalArgumentException("O arquivo SQL está vazio ou contém apenas comentários/linhas em branco.");
            }
            System.out.println(DateUtils.getCurrentTimestamp() + " - Executando query...");
        	System.out.println(); 
            try (PreparedStatement ps = conn.prepareStatement(sqlQuery);
                 ResultSet rs = ps.executeQuery()) {

                ResultSetMetaData rsmd = rs.getMetaData();
                int columnCount = rsmd.getColumnCount();

                // Gerar e escrever cabeçalho
                StringBuilder header = new StringBuilder();
                for (int i = 1; i <= columnCount; i++) {
                    header.append(enclosureCharacter).append(rsmd.getColumnName(i)).append(enclosureCharacter);
                    if (i < columnCount) {
                        header.append(columnSeparator);
                    }
                }
                pw.println(header.toString());
                System.out.println(DateUtils.getCurrentTimestamp() + " - Escrevendo arquivo...");
            	System.out.println(); 
                // Escrever dados
                while (rs.next()) {
                    StringBuilder row = new StringBuilder();
                    for (int i = 1; i <= columnCount; i++) {
                        Object value = rs.getObject(i);
                        int columnType = rsmd.getColumnType(i);
                        String formattedValue = formatValue(value, columnType, dateFormat, decimalFormat, decimalSeparator);
                        row.append(enclosureCharacter).append(formattedValue).append(enclosureCharacter);
                        if (i < columnCount) {
                            row.append(columnSeparator);
                        }
                    }
                    pw.println(row.toString());
                }
                System.out.println(DateUtils.getCurrentTimestamp() + " - Arquivo finalizado...");
            	System.out.println(); 
            } catch (SQLException e) {
                System.err.println("Erro Oracle: Código - " + e.getErrorCode() + ", Mensagem - " + e.getMessage());
                printQueryContext(e.getMessage(), sqlQuery);
                System.exit(1);
            } catch (Exception e) {
                e.printStackTrace(); // Para outros tipos de exceções
                System.exit(1);
            }
        	System.out.println(); 
        }
    }
    private void printQueryContext(String errorMessage, String sqlQuery) {
        // Divida a query em palavras
        String[] queryWords = sqlQuery.split("\\s+");
        // Divida a mensagem de erro em palavras
        String[] errorWords = errorMessage.split("\\s+");

        // Procure palavras da mensagem de erro na query
        for (String errorWord : errorWords) {
            for (int i = 0; i < queryWords.length; i++) {
                if (queryWords[i].equalsIgnoreCase(errorWord)) {
                    // Encontrou uma correspondência, imprima o contexto
                    int start = Math.max(i - 5, 0); // 5 palavras antes
                    int end = Math.min(i + 5, queryWords.length - 1); // 5 palavras depois
                    System.err.println("Contexto do erro na query:");
                    for (int j = start; j <= end; j++) {
                        System.err.print(queryWords[j] + " ");
                    }
                    System.err.println("\n...");
                    return; // Encerra após encontrar a primeira correspondência
                }
            }
        }
    }

    private String formatValue(Object value, int columnType, String dateFormat, String decimalFormatPattern, String decimalSeparator) {
        if (value == null) {
            return "";
        } else if (columnType == Types.DATE || columnType == Types.TIMESTAMP) {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            return sdf.format(value);
        } else if (columnType == Types.DECIMAL || columnType == Types.NUMERIC || columnType == Types.FLOAT || columnType == Types.DOUBLE) {
            DecimalFormat df = new DecimalFormat(decimalFormatPattern);

            char separatorChar = decimalSeparator.charAt(0);
            DecimalFormatSymbols symbols = new DecimalFormatSymbols();
            symbols.setDecimalSeparator(separatorChar);
            df.setDecimalFormatSymbols(symbols);

            return df.format(value);
        } else {
            return value.toString();
        }
    }
}
