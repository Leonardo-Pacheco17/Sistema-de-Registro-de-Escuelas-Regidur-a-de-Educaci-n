/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package escuelasframe;

/**
 *
 * @author Golem
 */

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.swing.JOptionPane;

public class ConexionBD {
   private static final String URL = "jdbc:mysql://localhost:3306/regiduria_educacion?useSSL=false&serverTimezone=UTC";
   private static final String USER = "root";          
   private static final String PASSWORD = "123456"; 

   public static Connection getConexion() {
        Connection con = null;
       try {
           Class.forName("com.mysql.cj.jdbc.Driver");
           con = DriverManager.getConnection(URL, USER, PASSWORD);
        } catch (ClassNotFoundException e) {
           JOptionPane.showMessageDialog(null,
              "No se encontró el driver de MySQL.\nRevisa que agregaste el .jar del conector.\n" + e.getMessage());
        } catch (SQLException e) {
          JOptionPane.showMessageDialog(null,
             "Error al conectar con la base de datos.\nRevisa URL, usuario y contraseña.\n" + e.getMessage());
        }
        return con;
    }
}
