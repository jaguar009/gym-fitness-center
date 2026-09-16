package pe.cibertec.gym.controller;

import pe.cibertec.gym.model.Membership;
import pe.cibertec.gym.repository.MemberRepository;
import pe.cibertec.gym.repository.MembershipRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/memberships")
public class MembershipController {
    private final MembershipRepository repository;
    private final MemberRepository memberRepository;

    public MembershipController(MembershipRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("memberships", repository.findAllByOrderByFechaFinAsc());
        return "memberships/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String form(Model model) {
        model.addAttribute("membership", new Membership());
        model.addAttribute("members", memberRepository.findAll());
        return "memberships/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String save(Membership membership, Long memberId) {
        membership.setMember(memberRepository.findById(memberId).orElseThrow());
        repository.save(membership);
        return "redirect:/memberships";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("membership", repository.findById(id).orElseThrow());
        model.addAttribute("members", memberRepository.findAll());
        return "memberships/form";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/memberships";
    }
}
