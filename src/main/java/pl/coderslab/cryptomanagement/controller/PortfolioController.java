package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.entity.PortfolioHistory;
import pl.coderslab.cryptomanagement.service.PortfolioService;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PortfolioController{
    private final PortfolioService portfolioService;
    private Portfolio portfolio;

    public PortfolioController(PortfolioService portfolioService, UserService userService, WalletService walletService) {
        this.portfolioService = portfolioService;
    }

    @GetMapping("/home")
    public String goHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        this.portfolio = portfolioService.getPortfolio(userDetails);
        model.addAttribute("totalValue", portfolio.getTotalValue());

        portfolioService.update(portfolio.getPortfolioId(), portfolio.getTotalValue());

        int assets = portfolioService.getTotalAssetsForUser(userDetails);
        model.addAttribute("assets", assets);

        LocalDateTime lastUpdate = portfolio.getUpdatedAt();
        model.addAttribute("lastUpdate", lastUpdate.toString());

        String highestValuedName = portfolioService.getHighestValueAssetForUser(userDetails);
        if (highestValuedName != null) {
            model.addAttribute("highestValue", highestValuedName);

        }
        return "index";
    }

    @GetMapping("/latest")
    @ResponseBody
    public List<PortfolioHistory> getUserLatestPortfolios() {
        return portfolioService.getLatestEntries(portfolio.getPortfolioId(), 7);
    }

}
