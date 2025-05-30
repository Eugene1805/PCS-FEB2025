package javafxappescolar.modelo.pojo;

/**
 *
 * @author eugen
 */
public class Alumno {
    
    private int idAlumno;
    private String nombre;
    private String apellidoPaterno;
    private String apellidoMaterno;
    private String matricula;
    private String email;
    private String fechaNacimeinto;
    private int idCarrera;
    private String carrera;
    private int idFacultad;
    private String facultad;
    private byte[] foto;

    public Alumno() {
    }

    public Alumno(int idAlumno, String nombre, String apellidoPaterno, String apellidoMaterno, String matricula, String email, String fechaNacimeinto, int idCarrera, String carrera, int idFacultad, String facultad, byte[] foto) {
        this.idAlumno = idAlumno;
        this.nombre = nombre;
        this.apellidoPaterno = apellidoPaterno;
        this.apellidoMaterno = apellidoMaterno;
        this.matricula = matricula;
        this.email = email;
        this.fechaNacimeinto = fechaNacimeinto;
        this.idCarrera = idCarrera;
        this.carrera = carrera;
        this.idFacultad = idFacultad;
        this.facultad = facultad;
        this.foto = foto;
    }

    public int getIdAlumno() {
        return idAlumno;
    }

    public void setIdAlumno(int idAlumno) {
        this.idAlumno = idAlumno;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getApellidoPaterno() {
        return apellidoPaterno;
    }

    public void setApellidoPaterno(String apellidoPaterno) {
        this.apellidoPaterno = apellidoPaterno;
    }

    public String getApellidoMaterno() {
        return apellidoMaterno;
    }

    public void setApellidoMaterno(String apellidoMaterno) {
        this.apellidoMaterno = apellidoMaterno;
    }

    public String getMatricula() {
        return matricula;
    }

    public void setMatricula(String matricula) {
        this.matricula = matricula;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFechaNacimeinto() {
        return fechaNacimeinto;
    }

    public void setFechaNacimeinto(String fechaNacimeinto) {
        this.fechaNacimeinto = fechaNacimeinto;
    }

    public int getIdCarrera() {
        return idCarrera;
    }

    public void setIdCarrera(int idCarrera) {
        this.idCarrera = idCarrera;
    }

    public String getCarrera() {
        return carrera;
    }

    public void setCarrera(String carrera) {
        this.carrera = carrera;
    }

    public int getIdFacultad() {
        return idFacultad;
    }

    public void setIdFacultad(int idFacultad) {
        this.idFacultad = idFacultad;
    }

    public String getFacultad() {
        return facultad;
    }

    public void setFacultad(String facultad) {
        this.facultad = facultad;
    }

    public byte[] getFoto() {
        return foto;
    }

    public void setFoto(byte[] foto) {
        this.foto = foto;
    }
    
    
    
    
    
}   
