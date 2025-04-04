package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import pl.coderslab.cryptomanagement.dto.CoinDTO;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.CoinService;

@Controller
public class CoinController extends GenericController<Coin> {
    private final CoinService coinService;

    public CoinController(CoinService coinService) {
        super(coinService, Coin.class);
        this.coinService = coinService;
    }

    @PatchMapping("/coin")
    public ResponseEntity<Coin> updateCoin() {
        coinService.update(1L, new CoinDTO());
        return ResponseEntity.ok().build();
    }


}
