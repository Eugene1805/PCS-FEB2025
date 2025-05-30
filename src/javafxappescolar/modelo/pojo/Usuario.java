package javafxappescolar.modelo.pojo;

/**
 *
 * @author eugen
 */
public class Usuario {
    private int idUsuario;
    private String nombre;
    private String apellido_paterno;
    private String apellido_materno;
    private String username;
    private String password;

    public Usuario() {
    }

    public Usuario(int idUsuario, String nombre, String apellido_paterno, String apellido_materno, String username) {
        this.idUsuario = idUsuario;
        this.nombre = nombre;
        this.apellido_paterno = apellido_paterno;
        this.apellido_materno = apellido_materno;
        this.username = username;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellido_paterno() {
        return apellido_paterno;
    }

    public void setApellido_paterno(String apellido_paterno) {
        this.apellido_paterno = apellido_paterno;
    }

    public String getApellido_materno() {
        return apellido_materno;
    }

    public void setApellido_materno(String apellido_materno) {
        this.apellido_materno = apellido_materno;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    @Override
    public String toString() {
        return nombre +" "+ apellido_paterno + " " + apellido_materno;
    }
    
    
    
}