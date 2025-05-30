package javafxappescolar.controlador;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafxappescolar.JavaFXAppEscolar;
import javafxappescolar.modelo.dao.InicioSesionDAO;
import javafxappescolar.modelo.pojo.Usuario;
import javafxappescolar.utilidades.Utilidad;

/**
 * FXML Controller class
 *
 * @author eugen
 */
public class FXMLInicioSesionController implements Initializable {

    @FXML
    private TextField tfUsuario;
    @FXML
    private PasswordField tfPassword;
    @FXML
    private Label lbErrorUsuario;
    @FXML
    private Label lbErrorPassword;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
            
    }    

    @FXML
    private void btnAutenticar(ActionEvent event) {
        String username = tfUsuario.getText();
        String password = tfPassword.getText();
        
        if(validarCampos(username, password)){
            validarCredenciales(username, password);
        }
        
    }
    
    private boolean validarCampos(String username, String password){
        boolean camposValidos = true;
        lbErrorUsuario.setText("");
        lbErrorPassword.setText("");
        if(username.isEmpty()){
            lbErrorUsuario.setText("Campo usuario obligatorio");
            camposValidos = false;
        }
        if(password.isEmpty()){
            lbErrorPassword.setText("Campo password obligatorio");
            camposValidos = false;
        }     
        return camposValidos;
    }
    
    private void validarCredenciales(String username, String password){
        try {
            Usuario usuarioSesion = InicioSesionDAO.verificarCredenciales(username,password);
            if(usuarioSesion != null){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Credenciales correctas",
                        "Bienvenido(a) " + usuarioSesion.toString() + " al sistema");
                irPantallaPrincipal(usuarioSesion);
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Credenciales incorrectas",
                        "Usuario y/o password incorrectos, por favor verifica tu informacion");
            }
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Problemas de conexion", 
                    ex.getMessage());
        }
    }
    
    private void irPantallaPrincipal(Usuario usuario){
        try {
            Stage escenarioBase = (Stage) tfUsuario.getScene().getWindow();
            FXMLLoader cargador = new FXMLLoader(JavaFXAppEscolar.class.getResource("vista/FXMLPrincipal.fxml"));
            Parent vista = cargador.load();
            
            FXMLPrincipalController controlador = cargador.getController();
            controlador.iniciarlizarInformacion(usuario);
            
            Scene escenaPrincipal = new Scene(vista);
            escenarioBase.setScene(escenaPrincipal);
            escenarioBase.setTitle("Home");
            escenarioBase.showAndWait();
        } catch (IOException ex) {
            ex.getCause();
        }
                
    }
}
