package javafxappescolar.modelo;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author eugen
 */
public class ConexionBD {
    
    private static final String IP = "localhost";
    private static final String PUERTO = "3306";
    private static final String NOMBRE_BD = "escolar";
    private static final String USUARIO = "controlEscolar";
    private static final String PASSWORD = "123456";
    private static final String DRIVER = "com.mysql.cj.jdbc.Driver";
    
    public static Connection abrirConexion(){
        Connection conexionBD = null;
        String urlConexion = 
                 String.format("jdbc:mysql://%s:%s/%s?allowPublicKeyRetrieval=true&useSSL=false&serverTimezone=UTC", IP, PUERTO, NOMBRE_BD);

        try{
            Class.forName(DRIVER);
            conexionBD = DriverManager.getConnection(urlConexion,USUARIO,PASSWORD);
                    
        }catch (ClassNotFoundException | SQLException ex) {
            Logger.getLogger(ConexionBD.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return conexionBD;
    }
}