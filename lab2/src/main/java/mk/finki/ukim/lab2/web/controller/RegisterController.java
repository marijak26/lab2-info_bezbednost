package mk.finki.ukim.lab2.web.controller;

import mk.finki.ukim.lab2.model.enumerations.Role;
import mk.finki.ukim.lab2.service.UserService;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

@Controller
@RequestMapping("/register")
public class RegisterController {

    private final UserService userService;
    private final JavaMailSender mailSender;
    private final Map<String, String> verificationCodes = new HashMap<>(); // Stores verification codes

    public RegisterController(UserService userService, JavaMailSender mailSender) {
        this.userService = userService;
        this.mailSender = mailSender;
    }

    @GetMapping
    public String getRegisterPage(@RequestParam(required = false) String error, Model model) {
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "register";
    }

    @PostMapping
    public String register(@RequestParam String username,
                           @RequestParam String password,
                           @RequestParam String repeatedPassword,
                           @RequestParam String name,
                           @RequestParam String surname,
                           @RequestParam Role role) {
        try {
            this.userService.register(username, password, repeatedPassword, name, surname, role);

            // Generate a unique verification code
            String verificationCode = UUID.randomUUID().toString();
            verificationCodes.put(username, verificationCode);

            // Send verification email
            sendVerificationEmail(username, verificationCode);

            return "redirect:/verify?email=" + username;
        } catch (RuntimeException ex) {
            return "redirect:/register?error=" + ex.getMessage();
        }
    }

    private void sendVerificationEmail(String email, String verificationCode) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setSubject("Email Verification");
        message.setText("Welcome! Please verify your email using the following code: " + verificationCode);
        mailSender.send(message);
    }

    @GetMapping("/verify")
    public String getVerificationPage(@RequestParam String email, @RequestParam(required = false) String error, Model model) {
        model.addAttribute("email", email);
        if (error != null && !error.isEmpty()) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", error);
        }
        return "verify";
    }

    @PostMapping("/verify")
    public String verifyUser(@RequestParam String email, @RequestParam String code) {
        String storedCode = verificationCodes.get(email);
        if (storedCode != null && storedCode.equals(code)) {
            verificationCodes.remove(email);
            // User is verified, you can update the user record here if necessary.
            userService.verifyUser(email);
            return "redirect:/login?success=Email verified successfully!";
        } else {
            return "redirect:/register/verify?email=" + email + "&error=Invalid verification code";
        }
    }
}
