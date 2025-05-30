package javafxappescolar.utilidades;

import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Control;
import javafx.stage.Stage;

/**
 *
 * @author eugen
 */
public class Utilidad {
    public static void mostrarAlertaSimple(Alert.AlertType tipo, String titulo, String contenido){
        Alert alerta = new Alert(tipo);
                alerta.setTitle(titulo);
                alerta.setHeaderText(null);
                alerta.setContentText(contenido);
                alerta.showAndWait();
    }
    
    public static Stage getEscenario(Control component){
        return ((Stage) component.getScene().getWindow());
    }
    
    public static boolean mostrarAlertaConfirmacion(String titulo, String contenido){
        Alert alertaConfirmacion = new Alert(Alert.AlertType.CONFIRMATION);
        alertaConfirmacion.setTitle(titulo);
        alertaConfirmacion.setHeaderText(null);
        alertaConfirmacion.setContentText(contenido);
        return alertaConfirmacion.showAndWait().get() == ButtonType.OK;
    }
}
