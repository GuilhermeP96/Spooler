package com.spooler.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.swing.JOptionPane;

public class ConnectionManager {

    private final String dbUrl;
    private final String dbUser;
    private final String dbPassword;

    public ConnectionManager(String dbUrl, String dbUser, String dbPassword) {
        this.dbUrl = dbUrl;
        this.dbUser = dbUser;
        this.dbPassword = dbPassword;
    }

    public Connection openConnection() throws SQLException {
        try {
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            throw new SQLException("Oracle JDBC Driver not found.", e);
        }
        Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPassword);
        conn.setAutoCommit(false); // Definir autoCommit como false explicitamente
        return conn;
    }

    
    public boolean isPasswordExpiring(String username, String dbInstance) {
        String sql = "SELECT username, TO_CHAR(TRUNC(expiry_date), 'DD/MM/YYYY') AS DATA_EXPIRACAO FROM user_users WHERE UPPER(username) = UPPER(?)";

        try (Connection conn = this.openConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setString(1, username);
            conn.setAutoCommit(false);
            
            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    String expiracao = rs.getString("DATA_EXPIRACAO");
                    SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
                    Date expiryDate = sdf.parse(expiracao);
                    Date currentDate = new Date();

                    // Calcular a diferença em dias
                    long diffInMillies = Math.abs(expiryDate.getTime() - currentDate.getTime());
                    long diff = diffInMillies / (24 * 60 * 60 * 1000);
                    System.out.println("Tempo para expirar a senha: "+diff);
                    if (diff <= 7) {
                        // Mostrar aviso se faltarem 7 dias ou menos
                        JOptionPane.showMessageDialog(null, 
                            "Aviso: A senha para o usuário " + username + " na instância " + dbInstance + 
                            " está prestes a expirar em " + diff + " dias. Por favor, atualize sua senha em breve para evitar interrupções.",
                            "Aviso de Expiração de Senha",
                            JOptionPane.WARNING_MESSAGE);
                        System.out.println("A senha para o usuário " + username + " na instância "+ dbInstance +" expira em: " + expiracao);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    // Implement other connection management methods as needed, such as closeConnection()
}
