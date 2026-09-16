package pe.cibertec.gym.controller;

import pe.cibertec.gym.model.GymClass;
import pe.cibertec.gym.repository.EmployeeRepository;
import pe.cibertec.gym.repository.GymClassRepository;
import pe.cibertec.gym.repository.MemberRepository;
import pe.cibertec.gym.service.EnrollmentService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/classes")
public class ClassController {
    private final GymClassRepository repository;
    private final EmployeeRepository employeeRepository;
    private final MemberRepository memberRepository;
    private final EnrollmentService enrollmentService;

    public ClassController(GymClassRepository repository, EmployeeRepository employeeRepository,
                           MemberRepository memberRepository, EnrollmentService enrollmentService) {
        this.repository = repository;
        this.employeeRepository = employeeRepository;
        this.memberRepository = memberRepository;
        this.enrollmentService = enrollmentService;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("classes", repository.findAllByOrderByNombreAsc());
        return "classes/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String form(Model model) {
        model.addAttribute("gymClass", new GymClass());
        model.addAttribute("employees", employeeRepository.findAll());
        return "classes/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String save(GymClass gymClass, Long employeeId) {
        if (employeeId != null) {
            gymClass.setEmployee(employeeRepository.findById(employeeId).orElseThrow());
        }
        repository.save(gymClass);
        return "redirect:/classes";
    }

    @GetMapping("/{id}/edit")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("gymClass", repository.findById(id).orElseThrow());
        model.addAttribute("employees", employeeRepository.findAll());
        return "classes/form";
    }

    @PostMapping("/{id}/delete")
    @PreAuthorize("hasRole('ADMIN')")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/classes";
    }

    @GetMapping("/{id}/enroll")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String enrollmentForm(@PathVariable Long id, Model model) {
        model.addAttribute("gymClass", repository.findById(id).orElseThrow());
        model.addAttribute("members", memberRepository.findAll());
        return "classes/enroll";
    }

    @PostMapping("/{id}/enroll")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String enroll(@PathVariable Long id, @RequestParam Long memberId, RedirectAttributes attributes) {
        try {
            enrollmentService.enroll(id, memberId);
            attributes.addFlashAttribute("success", "Inscripción registrada correctamente");
        } catch (RuntimeException ex) {
            attributes.addFlashAttribute("error", ex.getMessage());
        }
        return "redirect:/classes";
    }
}
