package mk.finki.ukim.lab2.web.controller;

import mk.finki.ukim.lab2.model.User;
import mk.finki.ukim.lab2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.Random;

@Controller
public class RegisterController {

    @Autowired
    private AuthService authService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String register(@ModelAttribute User user, Model model) {
        if (authService.isEmailRegistered(user.getEmail())) {
            model.addAttribute("error", "Email is already registered.");
            return "register";
        }

        String verificationCode = String.format("%06d", new Random().nextInt(1000000));
        authService.saveVerificationCode(user.getUsername(), verificationCode);

        authService.saveUser(user);

        model.addAttribute("verificationCode", verificationCode);

        return "verification";
    }
}






