package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.dto.PortfolioDTO;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.PortfolioService;

@Controller
@RequestMapping("/portfolio")
public class PortfolioController extends GenericController<Portfolio> {
    private final PortfolioService portfolioService;

    public PortfolioController(PortfolioService portfolioService) {
        super(portfolioService, Portfolio.class);
        this.portfolioService = portfolioService;
    }

    @PatchMapping("/portfolio")
    public ResponseEntity<Portfolio> updatePortfolio() {
        portfolioService.update(1L, new PortfolioDTO());
        return ResponseEntity.ok().build();
    }


}
