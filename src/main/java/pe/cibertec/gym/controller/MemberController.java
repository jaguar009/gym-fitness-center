package pe.cibertec.gym.controller;

import pe.cibertec.gym.model.Member;
import pe.cibertec.gym.repository.MemberRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/members")
public class MemberController {
    private final MemberRepository repository;

    public MemberController(MemberRepository repository) { this.repository = repository; }

    @GetMapping
    public String list(@RequestParam(required = false) String q, @RequestParam(required = false) String status, Model model) {
        model.addAttribute("members", repository.search(normalize(q), normalize(status)));
        model.addAttribute("q", q == null ? "" : q);
        model.addAttribute("status", status == null ? "" : status);
        return "members/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String createForm(Model model) {
        model.addAttribute("member", new Member());
        return "members/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String save(Member member) {
        repository.save(member);
        return "redirect:/members";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String editForm(@PathVariable Long id, Model model) {
        model.addAttribute("member", repository.findById(id).orElseThrow());
        return "members/form";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/members";
    }

    private String normalize(String value) { return value == null || value.isBlank() ? null : value.trim(); }
}
