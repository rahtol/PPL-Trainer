
package com.becke.quizappfx.dao.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class MysqlConnection {
    private static Connection con;
    
    public MysqlConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost/quiz_app_db", "root", "Werder,00");
            
        } catch (ClassNotFoundException | SQLException ex) {
            System.out.println("Error msg: " + ex.getMessage());
            con = null;
        }
    }
    
    public Connection getConnection() {
        return con;
    }
}
