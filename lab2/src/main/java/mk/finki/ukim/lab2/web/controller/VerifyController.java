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
public class VerifyController {

    @Autowired
    private AuthService authService;

    @GetMapping("/verify")
    public String verificationForm(Model model) {
        model.addAttribute("user", new User());
        return "verification";
    }

    @PostMapping("/verify")
    public String verifyUser(@ModelAttribute User user, String verificationCode, Model model) {
        String storedCode = authService.getVerificationCode(user.getUsername());
        if (storedCode != null && storedCode.equals(verificationCode)) {
            authService.verifyUser(user.getUsername());
            model.addAttribute("message", "Registration successful! Please log in.");
            return "login";
        } else {
            model.addAttribute("error", "Invalid verification code.");
            return "verification";
        }
    }
}





