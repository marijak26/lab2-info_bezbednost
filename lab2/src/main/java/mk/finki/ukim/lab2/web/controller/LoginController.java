package mk.finki.ukim.lab2.web.controller;

import mk.finki.ukim.lab2.model.User;
import mk.finki.ukim.lab2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class LoginController {

    @Autowired
    private AuthService authService;

    @GetMapping("/login")
    public String loginForm(Model model) {
        model.addAttribute("user", new User());
        return "login";
    }

    @PostMapping("/login")
    public String login(@ModelAttribute User user, Model model) {
        boolean isValid = authService.verifyPassword(user.getUsername(), user.getPassword());
        if (isValid) {
            if (!authService.isUserVerified(user.getUsername())) {
                model.addAttribute("error", "Please verify your email first.");
                return "login";
            }
            String twoFactorCode = authService.generateTwoFactorCode(user.getUsername());
            model.addAttribute("twoFactorCode", twoFactorCode);
            return "twoFactor";
        } else {
            model.addAttribute("error", "Invalid username or password.");
            return "login";
        }
    }
}



