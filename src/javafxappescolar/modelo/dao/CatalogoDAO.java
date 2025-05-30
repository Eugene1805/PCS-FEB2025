package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.pojo.Carrera;
import javafxappescolar.modelo.pojo.Facultad;

/**
 *
 * @author eugen
 */
public class CatalogoDAO {
    
    public static ArrayList<Facultad> obtenerFacultad() throws SQLException{
        ArrayList<Facultad> facultades = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT id_facultad, nombre FROM facultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                facultades.add(convertirRegistroFacultad(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("No se pudo conectar a la base de datos");
        }
        
        return facultades;
    }
    
    public static ArrayList<Carrera> obtenerCarreraPorFacultad(int idFacultad) throws SQLException{
        ArrayList<Carrera> carreras = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT id_carrera, nombre, codigo, id_facultad FROM carrera WHERE id_facultad = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idFacultad);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                carreras.add(convertirRegistroCarrera(resultado));
            }
        }else{
            throw new SQLException("No se pudo conectar a la base de datos");
        }
        return carreras;
    }

    private static Facultad convertirRegistroFacultad(ResultSet resultado) throws SQLException{
        Facultad facultad = new Facultad();
        facultad.setIdFacultad(resultado.getInt("id_facultad"));
        facultad.setNombre(resultado.getString("nombre"));
        return facultad;
    }

    private static Carrera convertirRegistroCarrera(ResultSet resultado) throws SQLException{
        Carrera carrera = new Carrera();
        carrera.setIdCarrera(resultado.getInt("id_carrera"));
        carrera.setNombre(resultado.getString("nombre"));
        carrera.setCodigo(resultado.getString("codigo"));
        return carrera;
    }
}
