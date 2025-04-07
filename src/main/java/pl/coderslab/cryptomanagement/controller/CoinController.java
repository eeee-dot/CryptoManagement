package pl.coderslab.cryptomanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.CoinService;

import java.util.List;

@Controller
@RequestMapping("/coin")
public class CoinController extends GenericController<Coin> {
    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        super(coinService, Coin.class);
        this.coinService = coinService;
    }

    @GetMapping()
    public String getCoins(Model model) {
        List<Coin> coins = coinService.getAll().getBody();
        model.addAttribute("coins", coins);
        return "coins";
    }


}
