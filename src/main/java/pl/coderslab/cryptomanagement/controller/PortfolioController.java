package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.PortfolioService;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;

import java.time.LocalDateTime;

@Controller
public class PortfolioController extends GenericController<Portfolio> {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService, UserService userService, WalletService walletService) {
        super(portfolioService, Portfolio.class);
        this.portfolioService = portfolioService;
    }

    @GetMapping("/home")
    public String goHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        Portfolio portfolio = portfolioService.getPortfolio(userDetails);
        model.addAttribute("totalValue", portfolio.getTotalValue());

        int assets = portfolioService.getTotalAssetsForUser(userDetails);
        model.addAttribute("assets", assets);

        LocalDateTime lastUpdate = portfolio.getUpdatedAt();
        model.addAttribute("lastUpdate", lastUpdate.toString());

        String highestValuedName = portfolioService.getHighestValueAssetForUser(userDetails);
        System.out.println(highestValuedName);
        if (highestValuedName != null) {
            model.addAttribute("highestValue", highestValuedName);

        }
        return "index";
    }

}
