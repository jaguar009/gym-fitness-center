package pe.cibertec.gym.controller;

import pe.cibertec.gym.model.Employee;
import pe.cibertec.gym.repository.EmployeeRepository;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/employees")
@PreAuthorize("hasRole('ADMIN')")
public class EmployeeController {
    private final EmployeeRepository repository;
    public EmployeeController(EmployeeRepository repository) { this.repository = repository; }

    @GetMapping
    public String list(Model model) {
        model.addAttribute("employees", repository.findAllByOrderByApellidosAscNombresAsc());
        return "employees/list";
    }

    @GetMapping("/new")
    public String form(Model model) {
        model.addAttribute("employee", new Employee());
        return "employees/form";
    }

    @PostMapping
    public String save(Employee employee) {
        repository.save(employee);
        return "redirect:/employees";
    }

    @GetMapping("/{id}/edit")
    public String edit(@PathVariable Long id, Model model) {
        model.addAttribute("employee", repository.findById(id).orElseThrow());
        return "employees/form";
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable Long id) {
        repository.deleteById(id);
        return "redirect:/employees";
    }
}
