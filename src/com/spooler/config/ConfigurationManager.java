package com.spooler.config;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Gerencia as configurações carregadas de um arquivo de propriedades.
 */
public class ConfigurationManager {

    private final Properties properties;

    public ConfigurationManager(String configFilePath) throws IOException {
        properties = new Properties();
        try (FileInputStream input = new FileInputStream(Paths.get(configFilePath).toFile())) {
            properties.load(input);
        }
        resolveEnvironmentVariables();
    }

    /**
     * Resolve as variáveis de ambiente dentro das propriedades.
     */
    private void resolveEnvironmentVariables() {
        Pattern pattern = Pattern.compile("\\$\\{(\\w+)\\}|\\$(\\w+)");

        for (String key : properties.stringPropertyNames()) {
            String value = properties.getProperty(key);
            Matcher matcher = pattern.matcher(value);
            StringBuffer buffer = new StringBuffer();

            while (matcher.find()) {
                String envVarName = matcher.group(1) != null ? matcher.group(1) : matcher.group(2);
                String envVarValue = System.getenv(envVarName);
                if (envVarValue == null) {
                    envVarValue = ""; // Se a variável de ambiente não está definida, substitua por uma string vazia
                }
                matcher.appendReplacement(buffer, envVarValue.replace("\\", "\\\\"));
            }
            matcher.appendTail(buffer);
            properties.setProperty(key, buffer.toString());
        }
    }

    /**
     * Obtém uma propriedade de configuração pelo nome.
     * @param name Nome da propriedade.
     * @return Valor da propriedade.
     */
    public String getProperty(String name) {
        return properties.getProperty(name);
    }
}
