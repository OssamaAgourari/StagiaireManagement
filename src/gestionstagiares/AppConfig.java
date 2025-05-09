/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package gestionstagiares;

import java.sql.*;

/**
 *
 * @author agour
 */
public class AppConfig {
    private final static String URL = "jdbc:mysql://localhost:3306/db_stagiaires";
    private final static String USERNAME = "root";
    private final static String PASSWORD = "";
    private static Connection cnx;

    public static Connection getConnection() {
        if (cnx == null) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                cnx = DriverManager.getConnection(URL, USERNAME, PASSWORD);
                return cnx;
            } catch (ClassNotFoundException | SQLException e) {
                throw new RuntimeException(e);
            }
        }
        return cnx;
    }
}
