package mk.finki.ukim.lab2.web.controller;

import mk.finki.ukim.lab2.model.AccessRequest;
import mk.finki.ukim.lab2.service.ResourceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ResourceController {

    @Autowired
    private ResourceService resourceService;

    @GetMapping("/access-management")
    public String getAccessManagementPage(Model model) {
        model.addAttribute("activeAccess", resourceService.getActiveAccess());
        return "access-management";
    }

    @PostMapping("/resource/request")
    public String requestResource(@ModelAttribute AccessRequest request) {
        resourceService.requestAccess(request);
        return "redirect:/access-management";
    }


    @PostMapping("/resource/revoke")
    public String revokeAccess(@RequestParam String userId, @RequestParam String resourceId) {
        resourceService.revokeAccess(userId, resourceId);
        return "redirect:/access-management";
    }
}
