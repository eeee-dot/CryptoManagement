package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.entity.PortfolioHistory;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.PortfolioService;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;

import java.time.LocalDateTime;
import java.util.List;

@Controller
public class PortfolioController extends GenericController<Portfolio> {
    private final PortfolioService portfolioService;
    private final UserService userService;
    private Portfolio portfolio;

    public PortfolioController(PortfolioService portfolioService, UserService userService, WalletService walletService) {
        super(portfolioService, Portfolio.class);
        this.portfolioService = portfolioService;
        this.userService = userService;
    }

    @GetMapping("/home")
    public String goHome(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        this.portfolio = portfolioService.getPortfolio(userDetails);
        model.addAttribute("totalValue", portfolio.getTotalValue());

        User user = userService.getUser(userDetails);
        portfolioService.update(portfolio.getPortfolioId(), portfolioService.getPortfolioValue(user));

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
