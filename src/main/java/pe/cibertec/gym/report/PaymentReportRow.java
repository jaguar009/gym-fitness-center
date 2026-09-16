package pe.cibertec.gym.report;

import java.math.BigDecimal;
import java.time.LocalDate;

public class PaymentReportRow {
    private final LocalDate fecha;
    private final String miembro;
    private final BigDecimal monto;
    private final String metodo;
    private final String concepto;

    public PaymentReportRow(LocalDate fecha, String miembro, BigDecimal monto, String metodo, String concepto) {
        this.fecha = fecha;
        this.miembro = miembro;
        this.monto = monto;
        this.metodo = metodo;
        this.concepto = concepto;
    }
    public LocalDate getFecha() { return fecha; }
    public String getMiembro() { return miembro; }
    public BigDecimal getMonto() { return monto; }
    public String getMetodo() { return metodo; }
    public String getConcepto() { return concepto; }
}
