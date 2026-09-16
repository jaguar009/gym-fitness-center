package pe.cibertec.gym.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "clase")
public class GymClass {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "idClase")
    private Long id;
    @Column(name = "nombre", nullable = false, length = 100)
    private String nombre;
    @Column(name = "descripcion")
    private String descripcion;
    @Column(name = "horario")
    private String horario;
    @Column(name = "cupos", nullable = false)
    private Integer cupos = 0;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "idEmpleado")
    private Employee employee;
    @Column(name = "estado", nullable = false, length = 20)
    private String estado = "ACTIVO";

    public GymClass() { }
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }
    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }
    public String getHorario() { return horario; }
    public void setHorario(String horario) { this.horario = horario; }
    public Integer getCupos() { return cupos; }
    public void setCupos(Integer cupos) { this.cupos = cupos; }
    public Employee getEmployee() { return employee; }
    public void setEmployee(Employee employee) { this.employee = employee; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }
}
