package pl.coderslab.cryptomanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.CoinService;

@Controller
@RequestMapping("/coin")
public class CoinController extends GenericController<Coin> {
    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        super(coinService, Coin.class);
        this.coinService = coinService;
    }

    @GetMapping("/coin")
    public String getCoins() {
        return "coins";
    }


}
