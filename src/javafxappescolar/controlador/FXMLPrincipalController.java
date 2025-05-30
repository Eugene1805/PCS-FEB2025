package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.pojo.Usuario;

/**
 * FXML Controller class
 *
 * @author eugen
 */
public class FXMLPrincipalController implements Initializable {

    private Usuario usuarioSesion;
    @FXML
    private Label lbNombre;
    @FXML
    private Label lbUsuario;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // TODO
    }    
    
    public void iniciarlizarInformacion(Usuario usuarioSesion){
        this.usuarioSesion = usuarioSesion;
        cargarInformacionUsuario();
    }
    
    private void cargarInformacionUsuario(){
        if (usuarioSesion != null){
            lbNombre.setText(usuarioSesion.toString());
            lbUsuario.setText(usuarioSesion.getUsername());
        }
    }

    @FXML
    private void btnClicCerrarSesion(ActionEvent event) {
        try{
            Stage escenarioBase =(Stage) lbNombre.getScene().getWindow();
            
            Parent vista = FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLInicioSesion.fxml"));
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Inicio Sesion");
            escenarioBase.showAndWait();
        }catch(IOException ex){
            ex.printStackTrace();
        }
    }

    @FXML
    private void btnClicAdminAlumno(ActionEvent event) {
        Stage escenarioAdmin = new Stage();
        try {
            Parent vista = FXMLLoader.load(JavaFXAppEscolar.class.getResource("vista/FXMLAdminAlumno.fxml"));
            Scene escenaAdminAlumno = new Scene(vista);
            escenarioAdmin.setScene(escenaAdminAlumno);
            escenarioAdmin.setTitle("Administracion Alumnos");
            escenarioAdmin.initModality(Modality.APPLICATION_MODAL);
            escenarioAdmin.showAndWait();
        } catch (IOException ex) {
            Logger.getLogger(FXMLPrincipalController.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
}
