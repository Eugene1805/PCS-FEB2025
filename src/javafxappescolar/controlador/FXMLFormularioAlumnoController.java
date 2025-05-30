package javafxappescolar.controlador;

import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import javafxappescolar.dominio.AlumnoDM;
import javafxappescolar.interfaz.INotificacion;
import javafxappescolar.modelo.dao.AlumnoDAO;
import javafxappescolar.modelo.dao.CatalogoDAO;
import javafxappescolar.modelo.pojo.Alumno;
import javafxappescolar.modelo.pojo.Carrera;
import javafxappescolar.modelo.pojo.Facultad;
import javafxappescolar.modelo.pojo.ResultadoOperacion;
import javafxappescolar.utilidades.Utilidad;
import javax.imageio.ImageIO;

/**
 * FXML Controller class
 *
 * @author eugen
 */
public class FXMLFormularioAlumnoController implements Initializable{

    @FXML
    private ImageView ivFoto;
    @FXML
    private Button clicSeleccionarFoto;
    @FXML
    private TextField tfNombre;
    @FXML
    private TextField tfApellidoPaterno;
    @FXML
    private TextField tfApellidoMaterno;
    @FXML
    private TextField tfMatricula;
    @FXML
    private TextField txEmail;
    @FXML
    private DatePicker dpFechaNacimiento;
    @FXML
    private ComboBox<Facultad> cbFacultad;
    @FXML
    private ComboBox<Carrera> cbCarrera;

    private ObservableList<Facultad> facultades;
    
    private ObservableList<Carrera> carreras;
    
    File archivoFoto;
    
    INotificacion observador;
    Alumno alumnoEdicion;
    boolean esEdicion;
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        cargarFaculdes();
        seleccionarFacultad();
        clicSeleccionarFoto.setOnAction(event -> mostrarDialogoSeleccion());
    }  
    
    public void inicializarInformacion(boolean esEdicion, Alumno alumnoEdicion, INotificacion observador){
        this.alumnoEdicion = alumnoEdicion;
        this.esEdicion = esEdicion;
        this.observador = observador;
        if(esEdicion){
            cargarInformacionEdicion();
        }
    }
    
    private void cargarInformacionEdicion(){
        tfMatricula.setText(alumnoEdicion.getMatricula());
        tfNombre.setText(alumnoEdicion.getNombre());
        tfApellidoPaterno.setText(alumnoEdicion.getApellidoPaterno());
        tfApellidoMaterno.setText(alumnoEdicion.getMatricula());
        txEmail.setText(alumnoEdicion.getEmail());
        dpFechaNacimiento.setValue(alumnoEdicion.getFechaNacimeinto() != null ?
                LocalDate.parse(alumnoEdicion.getFechaNacimeinto()) : LocalDate.now());
        tfMatricula.setDisable(false);
        cbFacultad.getSelectionModel().select(obtnerPosicionFacultad(alumnoEdicion.getIdFacultad()));
        cbCarrera.getSelectionModel().select(obtenerPosicionCarrera(alumnoEdicion.getIdCarrera()));
        
        try {
            byte[] foto = AlumnoDAO.obtenerFotoAlumno(alumnoEdicion.getIdAlumno());
            ByteArrayInputStream input = new ByteArrayInputStream(foto);
            Image image = new Image(input);
            ivFoto.setImage(image);
            alumnoEdicion.setFoto(foto);
        } catch (SQLException ex) {
            Logger.getLogger(FXMLFormularioAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }catch(NullPointerException e){
            Logger.getLogger(FXMLFormularioAlumnoController.class.getName()).log(Level.SEVERE, null, e);
        }
    }
    
    private void cargarFaculdes(){
        try {
            facultades = FXCollections.observableArrayList();
            ArrayList<Facultad> facultadesDAO = CatalogoDAO.obtenerFacultad();
            facultades.addAll(facultadesDAO);
            cbFacultad.setItems(facultades);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Por el momento no se"
                    + " pueden mostrar las facultades");
            cerrarVentana();        }
    }
    
    private void seleccionarFacultad(){
        cbFacultad.valueProperty().addListener(new ChangeListener<Facultad>(){
            @Override
            public void changed(ObservableValue<? extends Facultad> observable, Facultad oldValue, Facultad newValue) {
                if(newValue != null){
                    cargarCarrears(newValue.getIdFacultad());
                }
            }
        });
    }
    
    private void cargarCarrears(int idFacultad){
        try {
            carreras = FXCollections.observableArrayList();
            ArrayList<Carrera> carrerasDAO = CatalogoDAO.obtenerCarreraPorFacultad(idFacultad);
            carreras.addAll(carrerasDAO);
            cbCarrera.setItems(carreras);
        } catch (SQLException ex) {
            Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "Error al cargar", "Por el momento no se"
                    + " puede mostrar las carreras de las facultades" );
            cerrarVentana();        
        }
    }

    @FXML
    private void btnGuardar(ActionEvent event) {
        if(validarCampos()){
            try {
                if(!esEdicion){
                    ResultadoOperacion resultadoOperacion = AlumnoDM.verificarEstadoMatricula(tfMatricula.getText());
                    if(!resultadoOperacion.isError()){
                        Alumno alumno = obtenerAlumnoNuevo();
                        guardarAlumno(alumno);
                    }else{
                        Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Verificar datos",
                                resultadoOperacion.getMensaje());
                    }
                }else{
                    Alumno alumnoEdicion = obtenerAlumnoEdicion();
                    modificarAlumno(alumnoEdicion);
                }
            } catch (IOException ex) {
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No se pudo guardar la foto", 
                        "Lo sentimos no fue posible guardar la foto");
            }
        }
    }

    @FXML
    private void btnCancelar(ActionEvent event) {
        Utilidad.getEscenario(cbCarrera).close();
    }

    private void cerrarVentana() {
        Utilidad.getEscenario(cbCarrera).close();
    }
    
    private void mostrarDialogoSeleccion(){
        FileChooser dialogoSeleccion = new FileChooser();
        FileChooser.ExtensionFilter filtroIMG = new FileChooser.ExtensionFilter("Archivos JPG (.jpg)",
                                                                                "*.jpg");
        dialogoSeleccion.getExtensionFilters().add(filtroIMG);
        dialogoSeleccion.setTitle("Selecciona una foto");
        archivoFoto = dialogoSeleccion.showOpenDialog(Utilidad.getEscenario(clicSeleccionarFoto));
        mostrarFotoPerfirl(archivoFoto);
    }
    
    private void mostrarFotoPerfirl(File archivoFoto){
        try {
            BufferedImage bufferImg = ImageIO.read(archivoFoto);
            Image imagen = SwingFXUtils.toFXImage(bufferImg, null);
            ivFoto.setImage(imagen);
        } catch (IOException ex) {
            Logger.getLogger(FXMLFormularioAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private boolean validarCampos(){
        Map<TextField, Integer> longitudesMaximas = new HashMap<>();
        longitudesMaximas.put(tfApellidoMaterno, 15);
        longitudesMaximas.put(tfApellidoPaterno, 100);
        longitudesMaximas.put(tfMatricula, 50);
        longitudesMaximas.put(tfNombre, 50);
      
        
        List<TextField> camposConError = new ArrayList<>();
        
        for (Map.Entry<TextField, Integer> entry : longitudesMaximas.entrySet()) {
            TextField field = entry.getKey();
            int longitudMaxima = entry.getValue();
            String texto = field.getText() == null ? "" : field.getText().trim();

            if (texto.isEmpty()) {
                camposConError.add(field);
                field.setStyle("-fx-border-color: red;"); // Resaltar campo vacío
            } else if (texto.length() > longitudMaxima) {
                camposConError.add(field);
                field.setText(""); //Limpiar campo
                field.setStyle("-fx-border-color: orange;"); // Resaltar campo con longitud excedida
            } else {
                field.setStyle(""); // Restablecer estilo si es válido
            }
        }
        
        if (!camposConError.isEmpty()) {
            StringBuilder mensaje = new StringBuilder("Errores en los siguientes campos:\n");
            for (TextField field : camposConError) {
                mensaje.append("- ").append(field.getId() != null ? field.getId() : field.getPromptText())
                      .append("\n");
            }
            Utilidad.mostrarAlertaSimple(Alert.AlertType.WARNING, "Error de validación", mensaje.toString());
            return false;
        }
        return true;    
    }
    
    private Alumno obtenerAlumnoNuevo() throws IOException{
        Alumno alumnoNuevo = new Alumno();
        alumnoNuevo.setNombre(tfNombre.getText());
        alumnoNuevo.setApellidoPaterno(tfApellidoPaterno.getText());
        alumnoNuevo.setApellidoMaterno(tfApellidoMaterno.getText());
        alumnoNuevo.setMatricula(tfMatricula.getText());
        alumnoNuevo.setEmail(txEmail.getText());
        alumnoNuevo.setFechaNacimeinto(dpFechaNacimiento.getValue().toString());
        alumnoNuevo.setIdCarrera(cbCarrera.getSelectionModel().getSelectedItem().getIdCarrera());
        byte[] foto = Files.readAllBytes(archivoFoto.toPath());
        alumnoNuevo.setFoto(foto);
        return alumnoNuevo;
    }
    
    private void guardarAlumno(Alumno alumno){
        try {
            ResultadoOperacion resultadoOperacion = AlumnoDAO.RegistrarAlumno(alumno);
            if(!resultadoOperacion.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno" +alumno.getNombre() 
                        +"registrado con exito",resultadoOperacion.getMensaje());
                Utilidad.getEscenario(cbCarrera).close();
                observador.operacionExitosa("insertar", alumno.getNombre());
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No se pudo guardar al alumno",
                        resultadoOperacion.getMensaje());
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLFormularioAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private int obtnerPosicionFacultad(int idFacultad){
        for (int i = 0; i < facultades.size(); i++) {
            if(facultades.get(i).getIdFacultad() == idFacultad){
                return i;
            }
        }
        return -1;
    }
    
    private int obtenerPosicionCarrera(int idCarrera){
        for (int i = 0; i < carreras.size(); i++) {
            if(carreras.get(i).getIdCarrera() == idCarrera){
                return i;
            }
        }
        return -1;
    }
    
    private void modificarAlumno(Alumno alumno){
        try {
            ResultadoOperacion resultadoOperacion = AlumnoDAO.editarAlumno(alumno);
            if(!resultadoOperacion.isError()){
                Utilidad.mostrarAlertaSimple(Alert.AlertType.INFORMATION, "Alumno" +alumno.getNombre() 
                        +"registrado con exito",resultadoOperacion.getMensaje());
                Utilidad.getEscenario(cbCarrera).close();
                observador.operacionExitosa("insertar", alumno.getNombre());
            }else{
                Utilidad.mostrarAlertaSimple(Alert.AlertType.ERROR, "No se pudo guardar al alumno",
                        resultadoOperacion.getMensaje());
            }
        } catch (SQLException ex) {
            Logger.getLogger(FXMLFormularioAlumnoController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private Alumno obtenerAlumnoEdicion() throws IOException{
        Alumno alumno = new Alumno();
        alumno.setIdAlumno(alumnoEdicion.getIdAlumno());
        alumno.setNombre(tfNombre.getText());
        alumno.setApellidoPaterno(tfApellidoPaterno.getText());
        alumno.setApellidoMaterno(tfApellidoMaterno.getText());
        alumno.setMatricula(tfMatricula.getText());
        alumno.setEmail(txEmail.getText());
        alumno.setFechaNacimeinto(dpFechaNacimiento.getValue().toString());
        alumno.setIdCarrera(cbCarrera.getSelectionModel().getSelectedItem().getIdCarrera());
        if(archivoFoto != null){
            byte[] foto = Files.readAllBytes(archivoFoto.toPath());
            alumno.setFoto(foto);
        }else{
            alumno.setFoto(alumnoEdicion.getFoto());
        }
        return alumno;
    }
}
