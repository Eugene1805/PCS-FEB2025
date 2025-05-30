package javafxappescolar.dominio;

import java.sql.SQLException;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.pojo.ResultadoOperacion;

/**
 *
 * @author eugen
 */
public class AlumnoDM {
    public static ResultadoOperacion verificarEstadoMatricula(String matricula){
        ResultadoOperacion resultadoOperacion = new ResultadoOperacion();
        if(matricula.startsWith("s")){
            try {
                boolean existe = AlumnoDAO.verificarExistenciaMatricula(matricula);
                resultadoOperacion.setError(existe);
                if(existe){
                    resultadoOperacion.setMensaje("La matricula ya existe en los registros");
                }
            } catch (SQLException ex) {
                resultadoOperacion.setError(true);
                resultadoOperacion.setMensaje("Por el momento no se puede validar la matriucla, intente mas tarde");
            }
        }else{
            resultadoOperacion.setError(true);
            resultadoOperacion.setMensaje("La matricula no tiene el formato correcto");
        }        
        return resultadoOperacion;
    }
}
