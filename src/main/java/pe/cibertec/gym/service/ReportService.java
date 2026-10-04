package pe.cibertec.gym.service;

import pe.cibertec.gym.model.Member;
import pe.cibertec.gym.model.Payment;
import pe.cibertec.gym.repository.MemberRepository;
import pe.cibertec.gym.repository.PaymentRepository;
import pe.cibertec.gym.report.MemberReportRow;
import pe.cibertec.gym.report.PaymentReportRow;
import pe.cibertec.gym.report.SummaryReportRow;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import net.sf.jasperreports.engine.JasperCompileManager;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.InputStream;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Service
public class ReportService {
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;

    public ReportService(MemberRepository memberRepository, PaymentRepository paymentRepository) {
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
    }

    @Transactional(readOnly = true)
    public byte[] memberReport(String query, String status) {
        List<MemberReportRow> rows = memberRepository.search(normalize(query), normalize(status)).stream()
                .map(this::memberRow).toList();
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Reporte de miembros");
        params.put("FILTER_TEXT", "Búsqueda: " + display(query) + " | Estado: " + display(status));
        Map<String, BigDecimal> summary = new TreeMap<>();
        rows.forEach(row -> summary.merge(row.getEstado(), BigDecimal.ONE, BigDecimal::add));
        params.put("SUMMARY_DATASET", summaryData(summary));
        params.put("SUMMARY_TEXT", "Total de miembros en el resultado: " + rows.size());
        return export("jasper/member_report.jrxml", params, rows);
    }

    @Transactional(readOnly = true)
    public byte[] paymentReport(LocalDate from, LocalDate to, String method) {
        if (from == null || to == null || to.isBefore(from)) {
            throw new IllegalArgumentException("Ingrese un periodo válido para el reporte");
        }
        List<PaymentReportRow> rows = paymentRepository.search(from, to, normalize(method)).stream()
                .map(this::paymentRow).toList();
        Map<String, Object> params = new HashMap<>();
        params.put("REPORT_TITLE", "Reporte de pagos");
        params.put("FILTER_TEXT", "Periodo: " + from + " a " + to + " | Método: " + display(method));
        Map<String, BigDecimal> summary = new TreeMap<>();
        rows.forEach(row -> summary.merge(row.getMetodo() == null ? "SIN MÉTODO" : row.getMetodo(), row.getMonto(), BigDecimal::add));
        params.put("SUMMARY_DATASET", summaryData(summary));
        params.put("SUMMARY_TEXT", "Pagos: " + rows.size() + " | Total S/ " + rows.stream()
                .map(PaymentReportRow::getMonto).reduce(BigDecimal.ZERO, BigDecimal::add));
        return export("jasper/payment_report.jrxml", params, rows);
    }

    private byte[] export(String path, Map<String, Object> params, List<?> rows) {
        try (InputStream source = new ClassPathResource(path).getInputStream()) {
            JasperReport report = JasperCompileManager.compileReport(source);
            JasperPrint print = JasperFillManager.fillReport(report, params, new JRBeanCollectionDataSource(rows));
            return JasperExportManager.exportReportToPdf(print);
        } catch (Exception ex) {
            throw new IllegalStateException("No se pudo generar el reporte", ex);
        }
    }

    private JRBeanCollectionDataSource summaryData(Map<String, BigDecimal> summary) {
        return new JRBeanCollectionDataSource(summary.entrySet().stream()
                .map(entry -> new SummaryReportRow(entry.getKey(), entry.getValue())).toList());
    }

    private MemberReportRow memberRow(Member m) {
        return new MemberReportRow(m.getDni(), m.getNombres(), m.getApellidos(), m.getCorreo(), m.getTelefono(), m.getEstado());
    }

    private PaymentReportRow paymentRow(Payment p) {
        String name = p.getMember().getNombres() + " " + p.getMember().getApellidos();
        return new PaymentReportRow(p.getFechaPago(), name, p.getMonto(), p.getMetodoPago(), p.getConcepto());
    }

    private String normalize(String value) {
        return value == null || value.isBlank() ? null : value.trim();
    }

    private String display(String value) {
        return value == null || value.isBlank() ? "Todos" : value;
    }
}
