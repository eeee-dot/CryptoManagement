package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.dto.PortfolioDTO;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.PortfolioService;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/home")
public class PortfolioController extends GenericController<Portfolio> {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService, UserService userService, WalletService walletService) {
        super(portfolioService, Portfolio.class);
        this.portfolioService = portfolioService;
    }

    @GetMapping()
    public String goHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Portfolio portfolio = portfolioService.getPortfolio(userDetails);
        model.addAttribute("totalValue", portfolio.getTotalValue());

        return "index";
    }
}
