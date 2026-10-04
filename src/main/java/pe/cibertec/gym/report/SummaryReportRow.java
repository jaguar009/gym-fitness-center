package pe.cibertec.gym.report;

import java.math.BigDecimal;

public class SummaryReportRow {
    private final String categoria;
    private final BigDecimal valor;
    public SummaryReportRow(String categoria, BigDecimal valor) { this.categoria=categoria; this.valor=valor; }
    public String getCategoria() { return categoria; }
    public BigDecimal getValor() { return valor; }
}
