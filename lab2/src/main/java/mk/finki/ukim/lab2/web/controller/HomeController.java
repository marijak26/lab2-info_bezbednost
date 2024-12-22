package mk.finki.ukim.lab2.web.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping(path = {"/home"})
public class HomeController {
    @GetMapping
    public String getHomePage(Model model) {
        model.addAttribute("bodyContent", "home");
        return "home";
    }

    @GetMapping("/access_denied")
    public String getAccessDeniedPage(Model model) {
        model.addAttribute("bodyContent", "access-denied");
        return "access-denied";
    }

}

