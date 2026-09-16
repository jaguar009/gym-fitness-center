package pe.cibertec.gym.controller;

import pe.cibertec.gym.model.Payment;
import pe.cibertec.gym.repository.MemberRepository;
import pe.cibertec.gym.repository.PaymentRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import java.time.LocalDate;

@Controller
@RequestMapping("/payments")
public class PaymentController {
    private final PaymentRepository repository;
    private final MemberRepository memberRepository;

    public PaymentController(PaymentRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String list(@RequestParam(required = false) LocalDate from,
                       @RequestParam(required = false) LocalDate to,
                       @RequestParam(required = false) String method, Model model) {
        LocalDate start = from == null ? LocalDate.now().withDayOfMonth(1) : from;
        LocalDate end = to == null ? LocalDate.now() : to;
        model.addAttribute("payments", repository.search(start, end, normalize(method)));
        model.addAttribute("from", start);
        model.addAttribute("to", end);
        model.addAttribute("method", method == null ? "" : method);
        return "payments/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String form(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("members", memberRepository.findAll());
        return "payments/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String save(Payment payment, Long memberId) {
        payment.setMember(memberRepository.findById(memberId).orElseThrow());
        repository.save(payment);
        return "redirect:/payments";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/payments";
    }

    private String normalize(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
