package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.core.parameters.P;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.dto.PortfolioDTO;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.PortfolioService;

import javax.sound.sampled.Port;
import java.math.BigDecimal;

@Controller
@RequestMapping("/home")
public class PortfolioController extends GenericController<Portfolio> {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        super(portfolioService, Portfolio.class);
        this.portfolioService = portfolioService;
    }

    @GetMapping()
    public String goHome(Model model){
        Portfolio portfolio = new Portfolio();
        BigDecimal totalValue = portfolio.getTotalValue();
        if(totalValue== null) {
            totalValue = BigDecimal.valueOf(0);
        }
        model.addAttribute("totalValue", totalValue);
        return "index";
    }

    @PatchMapping("/portfolio")
    public ResponseEntity<Portfolio> updatePortfolio() {
        portfolioService.update(1L, new PortfolioDTO());
        return ResponseEntity.ok().build();
    }


}
