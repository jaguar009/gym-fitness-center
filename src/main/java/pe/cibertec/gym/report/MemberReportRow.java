package pe.cibertec.gym.report;

public class MemberReportRow {
    private final String dni;
    private final String nombres;
    private final String apellidos;
    private final String correo;
    private final String telefono;
    private final String estado;

    public MemberReportRow(String dni, String nombres, String apellidos, String correo, String telefono, String estado) {
        this.dni = dni;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.correo = correo;
        this.telefono = telefono;
        this.estado = estado;
    }
    public String getDni() { return dni; }
    public String getNombres() { return nombres; }
    public String getApellidos() { return apellidos; }
    public String getCorreo() { return correo; }
    public String getTelefono() { return telefono; }
    public String getEstado() { return estado; }
}
