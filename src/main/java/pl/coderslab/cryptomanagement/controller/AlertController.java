package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cryptomanagement.entity.Alert;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.AlertService;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import pl.coderslab.cryptomanagement.service.CoinService;
import pl.coderslab.cryptomanagement.service.UserService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/alert")
public class AlertController extends GenericController<Alert> {
    private final AlertService alertService;
    private final UserRepository userRepository;
    private final CoinService coinService;
    private final UserService userService;

    public AlertController(AlertService alertService, UserRepository userRepository, CoinService coinService, UserService userService) {
        super(alertService, Alert.class);
        this.alertService = alertService;
        this.userRepository = userRepository;
        this.coinService = coinService;
        this.userService = userService;
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

    @GetMapping("/add")
    public String addAlert() {
        return "add-alert-form";
    }

    @PostMapping("/add")
    public String addAlert(
            @RequestParam("coin") String name,
            @RequestParam("target-price") BigDecimal price,
            @RequestParam("status") Boolean status,
            @RequestParam("username") String username,
            Model model) {
        User user = userService.loadByUsername(username).getBody();
        Coin coin = coinService.loadByName(name).getBody();

        Alert alert = new Alert();
        alert.setUser(user);
        alert.setCoin(coin);
        alert.setStatus(status);
        alert.setPriceTarget(price);

        alertService.add(alert);
        model.addAttribute("message", "Wallet added successfully");
        return "redirect:/alert";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteAlert(@PathVariable Long id) {
        alertService.delete(id);
        return "alerts";
    }
}
