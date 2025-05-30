package javafxappescolar.modelo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import javafxappescolar.modelo.ConexionBD;
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author eugen
 */
public class AlumnoDAO {
    public static ArrayList<Alumno> obtenerAlumnos()throws SQLException{
        ArrayList<Alumno> alumnos = new ArrayList<>();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT id_alumno, a.nombre, apellido_paterno, apellido_materno, matricula, "
                    + "email, fecha_nacimiento, a.id_carrera, c.nombre AS 'carrera', f.id_facultad, "
                    + "f.nombre as 'facultad' "
                    + "FROM alumno a "
                    + "INNER JOIN carrera c ON c.id_carrera = a.id_carrera "
                    + "INNER JOIN facultad f ON f.id_facultad = c.id_facultad";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            ResultSet resultado = sentencia.executeQuery();
            while(resultado.next()){
                alumnos.add(convertirRegistroAlumno(resultado));
            }
            sentencia.close();
            resultado.close();
            conexionBD.close();
        }else{
            throw new SQLException("Sin conexion a la BD");
        }
        return alumnos;
    }
    
    public static ResultadoOperacion RegistrarAlumno(Alumno alumno) throws SQLException{ 
        ResultadoOperacion resultado = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "INSERT INTO alumno (nombre, apellido_paterno, apellido_materno, matricula, "
                    + "email, fecha_nacimiento, id_carrera, foto) VALUES (?,?,?,?,?,?,?,?)";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta,
                    PreparedStatement.RETURN_GENERATED_KEYS);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getMatricula());
            sentencia.setString(5, alumno.getEmail());
            sentencia.setString(6, alumno.getFechaNacimeinto());
            sentencia.setInt(7, alumno.getIdCarrera());
            sentencia.setBytes(8, alumno.getFoto());
            int filasAfectadas = sentencia.executeUpdate();
            resultado.setError(filasAfectadas <= 0);
            resultado.setMensaje(resultado.isError() ? 
            "Lo sentimos, no fue posible registrar al alumno." : 
            "Alumno(a) registrado(a) correctamente.");
        }else{
            throw new SQLException("Sin conexion a la base de datos");
        }
        return resultado;
    }
    
    public static byte[] obtenerFotoAlumno(int idAlumno) throws SQLException{
        byte[] foto = null;
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta ="SELECT foto FROM alumno WHERE id_alumno = ?";
            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setInt(1, idAlumno);
            ResultSet resultado = sentencia.executeQuery();
            if(resultado.next()){
                foto = resultado.getBytes("foto");
            }else{
                throw new SQLException("Sin conexion a la base de datos");
            }
        }
        return foto;
    }
    
    public static boolean verificarExistenciaMatricula(String matricula)throws SQLException{
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "SELECT COUNT(*) AS total FROM alumno WHERE matricula = ?";
            PreparedStatement sentencia = conexionBD.prepareCall(consulta);
            sentencia.setString(1, matricula);
            ResultSet resultado = sentencia.executeQuery();
            return resultado.getInt("total") > 0;
        }else{
            throw new SQLException("Sin conexion a la base de datos");
        }
    }
    
    public static ResultadoOperacion editarAlumno(Alumno alumno) throws SQLException {
        //TODO -- Restriccion no se puede editar la matricula
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "UPDATE alumno SET nombre = ?, apellido_paterno = ?, apellido_materno = ?, " +
             "email = ?, fecha_nacimiento = ?, id_carrera = ?, foto = ? WHERE id_alumno = ?";

            PreparedStatement sentencia = conexionBD.prepareStatement(consulta);
            sentencia.setString(1, alumno.getNombre());
            sentencia.setString(2, alumno.getApellidoPaterno());
            sentencia.setString(3, alumno.getApellidoMaterno());
            sentencia.setString(4, alumno.getEmail());
            sentencia.setString(5, alumno.getFechaNacimeinto());
            sentencia.setInt(6, alumno.getIdCarrera());
            sentencia.setBytes(7, alumno.getFoto());
            sentencia.setInt(8, alumno.getIdAlumno());
            
            int filasAfectadas = sentencia.executeUpdate();
            resultadoOperacion.setError(filasAfectadas <= 0);
            resultadoOperacion.setMensaje(resultadoOperacion.isError() ?
                    "Lo sentimos no fue posible actualizar los datos del alumno" :
                    "La informacion del alumno fue actualizada correctamente");
        }else{
            throw new SQLException("Sin conexion a la base de datos");
        }
        return resultadoOperacion;
    }
    
    public static ResultadoOperacion eliminarAlumno(int id)throws SQLException{
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        Connection conexionBD = ConexionBD.abrirConexion();
        if(conexionBD != null){
            String consulta = "DELETE FROM alumno WHERE id_alumno = ?";
            PreparedStatement sentencia = conexionBD.prepareCall(consulta);
            sentencia.setInt(1, id);
            int filasAfectadas = sentencia.executeUpdate();
            resultadoOperacion.setError(filasAfectadas <= 0);
            resultadoOperacion.setMensaje(resultadoOperacion.isError() ?
                    "Lo sentimos no fue posible eliminar al alumno" :
                    "La informacion del alumno fue eliminada correctamente");
        }else{
            throw new SQLException("Sin conexion a la base de datos");
        }
        return resultadoOperacion;
    }
    
    private static Alumno convertirRegistroAlumno(ResultSet resultado) throws SQLException{
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(resultado.getInt("id_alumno"));
        alumno.setNombre(resultado.getString("nombre"));
        alumno.setApellidoPaterno(resultado.getString("apellido_paterno"));
        alumno.setApellidoMaterno(resultado.getString("apellido_materno"));
        alumno.setMatricula(resultado.getString("matricula"));
        alumno.setEmail(resultado.getString("email"));
        alumno.setFechaNacimeinto(resultado.getString("fecha_nacimiento"));
        alumno.setIdCarrera(resultado.getInt("id_carrera"));
        alumno.setCarrera(resultado.getString("carrera"));
        alumno.setIdFacultad(resultado.getInt("id_facultad"));
        alumno.setFacultad(resultado.getString("facultad"));
        return alumno;
    }
}
