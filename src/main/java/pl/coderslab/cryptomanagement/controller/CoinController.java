package pl.coderslab.cryptomanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.*;

import java.util.List;



@Controller
@RequestMapping("/coin")
@SessionAttributes("walletId")
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

    @GetMapping("/add")
    public String addCoinView(Model model, @RequestParam Long walletId) {
        List<Coin> coins = coinService.getAll().getBody();
        if (coins != null) {
            List<String> coinNames = coins.stream().map(Coin::getName).toList();
            model.addAttribute("coinNames", coinNames);
        }

        model.addAttribute("walletId", walletId);
        return "add-coin-form";
    }
}
