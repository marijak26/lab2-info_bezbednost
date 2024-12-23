package mk.finki.ukim.lab2.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @GetMapping("/settings")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminSettings(Model model) {
        model.addAttribute("message", "Welcome to Admin Settings!");
        return "admin/settings";
    }

    @GetMapping("/reports")
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public String getAdminReports(Model model) {
        model.addAttribute("message", "Here are the admin reports.");
        return "admin/reports";
    }
}

