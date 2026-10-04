package pe.cibertec.gym.controller;

import pe.cibertec.gym.repository.MemberRepository;
import pe.cibertec.gym.repository.PaymentRepository;
import pe.cibertec.gym.service.ReportService;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@Controller
@RequestMapping("/reports")
public class ReportController {
    private final MemberRepository memberRepository;
    private final PaymentRepository paymentRepository;
    private final ReportService reportService;

    public ReportController(MemberRepository memberRepository, PaymentRepository paymentRepository, ReportService reportService) {
        this.memberRepository = memberRepository;
        this.paymentRepository = paymentRepository;
        this.reportService = reportService;
    }

    @GetMapping
    public String dashboard(Model model) {
        model.addAttribute("members", memberRepository.count());
        model.addAttribute("activeMembers", memberRepository.countByEstado("ACTIVO"));
        model.addAttribute("income", paymentRepository.totalAmount());
        return "reports/dashboard";
    }

    @GetMapping("/members")
    public String members(@RequestParam(required = false) String q, @RequestParam(required = false) String status, Model model) {
        model.addAttribute("rows", memberRepository.search(normalize(q), normalize(status)));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("status", status == null ? "" : status);
        return "reports/members";
    }

    @GetMapping("/members/pdf")
    public ResponseEntity<byte[]> membersPdf(@RequestParam(required = false) String q, @RequestParam(required = false) String status) {
        return pdf(reportService.memberReport(q, status), "reporte-miembros.pdf");
    }

    @GetMapping("/payments")
    public String payments(@RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
                           @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
                           @RequestParam(required = false) String method, Model model) {
        LocalDate start = from == null ? LocalDate.now().withDayOfMonth(1) : from;
        LocalDate end = to == null ? LocalDate.now() : to;
        if (end.isBefore(start)) throw new IllegalArgumentException("La fecha final debe ser posterior a la inicial");
        model.addAttribute("rows", paymentRepository.search(start, end, normalize(method)));
        model.addAttribute("from", start);
        model.addAttribute("to", end);
        model.addAttribute("method", method == null ? "" : method);
        return "reports/payments";
    }

    @GetMapping("/payments/pdf")
    public ResponseEntity<byte[]> paymentsPdf(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate from,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate to,
            @RequestParam(required = false) String method) {
        return pdf(reportService.paymentReport(from, to, method), "reporte-pagos.pdf");
    }

    private ResponseEntity<byte[]> pdf(byte[] content, String filename) {
        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + filename + "\"")
                .contentType(MediaType.APPLICATION_PDF)
                .body(content);
    }

    private String normalize(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
