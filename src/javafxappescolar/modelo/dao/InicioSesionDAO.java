package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.pojo.Usuario;

/**
 *
 * @author eugen
 */
public class InicioSesionDAO {
    
    public static Usuario verificarCredenciales(String username, String password)throws SQLException{
        Usuario usuarioSesion = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT id_usuario, nombre, apellido_paterno, apellido_materno, username FROM "
                    + "usuario WHERE username = ? AND password = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, username);
            sentencia.setString(2, password);
            ResultSet resultado =  sentencia.executeQuery();
            if(resultado.next()){
                usuarioSesion = convertirRegistroUsuario(resultado);
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Error:'Sin conexion a la base de datos'");
        }
        return usuarioSesion;
    }
    
    private static Usuario convertirRegistroUsuario(ResultSet resultado)throws SQLException{
        Usuario usuario = new Usuario();
        usuario.setIdUsuario(resultado.getInt("id_usuario"));
        usuario.setNombre(resultado.getString("nombre"));
        usuario.setApellido_paterno(resultado.getString("apellido_paterno"));
        usuario.setApellido_materno((resultado.getString("apellido_materno")) != null ? 
                resultado.getString("apellido_materno"): "");
        usuario.setUsername(resultado.getString("username"));
        return usuario;
    }
}