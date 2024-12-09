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
public class TwoFactorController {

    @Autowired
    private AuthService authService;

    @GetMapping("/two-factor")
    public String twoFactorForm(Model model) {
        model.addAttribute("user", new User());
        return "twoFactor";
    }

    @PostMapping("/two-factor")
    public String verifyTwoFactor(@ModelAttribute User user, String twoFactorCode, Model model) {
        boolean isValid = authService.verifyTwoFactorCode(user.getUsername(), twoFactorCode);

        if (isValid) {
            String sessionToken = generateSessionToken();
            model.addAttribute("sessionToken", sessionToken);
            model.addAttribute("message", "2FA successful! You are logged in.");
            authService.clearTwoFactorCode(user.getUsername());
            return "home";
        } else {
            model.addAttribute("error", "Invalid 2FA code. Please try again.");
            return "twoFactor";
        }
    }

    private String generateSessionToken() {
        return "SESSION_" + System.currentTimeMillis();
    }
}



