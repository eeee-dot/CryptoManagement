package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.entity.Alert;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.AlertService;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alert")
public class AlertController extends GenericController<Alert> {
    private final AlertService alertService;
    private final UserRepository userRepository;

    public AlertController(AlertService alertService, UserRepository userRepository) {
        super(alertService, Alert.class);
        this.alertService = alertService;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String getAlerts(Model model) {
        String currentPrincipalName = SecurityContextHolder.getContext().getAuthentication().getName();
        Optional<User> user = userRepository.findByUsername(currentPrincipalName);

        if (user.isEmpty()) {
            return "404";
        }

        List<Alert> alerts = alertService.loadAlertsByUser(user.get()).getBody();
        model.addAttribute("alerts", alerts);
        return "alerts";
    }



}
