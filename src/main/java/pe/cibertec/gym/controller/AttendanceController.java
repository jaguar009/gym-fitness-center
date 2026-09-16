package pe.cibertec.gym.controller;

import pe.cibertec.gym.model.Attendance;
import pe.cibertec.gym.repository.AttendanceRepository;
import pe.cibertec.gym.repository.MemberRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/attendance")
public class AttendanceController {
    private final AttendanceRepository repository;
    private final MemberRepository memberRepository;

    public AttendanceController(AttendanceRepository repository, MemberRepository memberRepository) {
        this.repository = repository;
        this.memberRepository = memberRepository;
    }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("attendance", repository.findAllByOrderByFechaDescIdDesc());
        return "attendance/list";
    }

    @GetMapping("/new")
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String form(Model model) {
        model.addAttribute("attendanceItem", new Attendance());
        model.addAttribute("members", memberRepository.findAll());
        return "attendance/form";
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ADMIN','RECEPCION')")
    public String save(Attendance attendance, Long memberId) {
        attendance.setMember(memberRepository.findById(memberId).orElseThrow());
        repository.save(attendance);
        return "redirect:/attendance";
    }
}
