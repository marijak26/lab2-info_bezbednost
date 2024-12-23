package mk.finki.ukim.lab2.web.controller;

import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/user")
public class UserController {

    @GetMapping("/dashboard")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    public String getUserDashboard(Model model) {
        model.addAttribute("message", "Welcome to the User Dashboard!");
        return "user/dashboard";
    }

    @GetMapping("/profile")
    @PreAuthorize("hasAnyRole('ROLE_USER','ROLE_MANAGER','ROLE_ADMIN')")
    public String getUserProfile(Model model) {
        model.addAttribute("message", "This is your user profile!");
        return "user/profile";
    }
}

