package mk.finki.ukim.lab2.web.controller;

import jakarta.servlet.http.HttpServletRequest;
import mk.finki.ukim.lab2.model.User;
import mk.finki.ukim.lab2.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/login")
public class LoginController {

    private final AuthService authService;
    private static final int MAX_SESSION_TIME_MINUTES = 60;

    public LoginController(AuthService authService) {
        this.authService = authService;
    }

    @GetMapping
    public String getLoginPage() {
        return "login";
    }

    @PostMapping
    public String login(HttpServletRequest request, Model model) {
        User user = null;
        String username = request.getParameter("username");
        String password = request.getParameter("password");

        try {
            user = authService.login(username, password);
            request.getSession().setAttribute("user", user);
            request.getSession().setAttribute("loginTime", LocalDateTime.now());

            if (isSessionExpired(request)) {
                request.getSession().invalidate();
                return "redirect:/login?error=sessionExpired";
            }

            return "redirect:/home";
        } catch (RuntimeException ex) {
            model.addAttribute("hasError", true);
            model.addAttribute("error", ex.getMessage());
            return "login";
        }
    }

    private boolean isSessionExpired(HttpServletRequest request) {
        LocalDateTime loginTime = (LocalDateTime) request.getSession().getAttribute("loginTime");
        if (loginTime == null) return true;

        return loginTime.plusMinutes(MAX_SESSION_TIME_MINUTES).isBefore(LocalDateTime.now());
    }
}




