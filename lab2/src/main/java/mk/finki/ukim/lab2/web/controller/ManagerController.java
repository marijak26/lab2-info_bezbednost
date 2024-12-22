package mk.finki.ukim.lab2.web.controller;


import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/manager")
public class ManagerController {

    @GetMapping("/overview")
    @PreAuthorize("hasRole('MANAGER')")
    public String getManagerOverview(Model model) {
        model.addAttribute("message", "Welcome to the Manager Overview!");
        return "manager/overview";
    }

    @GetMapping("/tasks")
    @PreAuthorize("hasRole('MANAGER')")
    public String getManagerTasks(Model model) {
        model.addAttribute("message", "Here are your management tasks!");
        return "manager/tasks";
    }
}

