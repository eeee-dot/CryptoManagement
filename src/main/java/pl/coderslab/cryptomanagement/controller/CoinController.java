package pl.coderslab.cryptomanagement.controller;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.SessionAttributes;
import pl.coderslab.cryptomanagement.api.CoinMarketCapAPI;
import pl.coderslab.cryptomanagement.entity.Coin;
<<<<<<< Updated upstream
import pl.coderslab.cryptomanagement.entity.Price;
import pl.coderslab.cryptomanagement.entity.WalletCoin;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.CoinService;
import pl.coderslab.cryptomanagement.service.PriceService;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;
=======
import pl.coderslab.cryptomanagement.service.*;
>>>>>>> Stashed changes

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;


@Controller
@RequestMapping("/coin")
@SessionAttributes("walletId")
public class CoinController {
    private final CoinService coinService;
    private final PriceService priceService;
    private final WalletService walletService;
    private final UserService userService;

<<<<<<< Updated upstream
    public CoinController(CoinService coinService, PriceService priceService, WalletService walletService, UserService userService) {
        super(coinService, Coin.class);
=======
    public CoinController(CoinService coinService) {
>>>>>>> Stashed changes
        this.coinService = coinService;
        this.priceService = priceService;
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping()
    public String getCoins(Model model) {
        List<Coin> coins = coinService.getAll().getBody();
        model.addAttribute("coins", coins);
        return "coins";
    }

    @GetMapping("/reload")
    public String reloadCoins() {
        JSONObject jsonResponse = CoinMarketCapAPI.getAPIResponse();
        if (jsonResponse != null) {
            JSONArray data = jsonResponse.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject coin = data.getJSONObject(i);

                Optional<Coin> optionalCoin = coinService.findByNameAndSymbol(coin.getString("name"), coin.getString("symbol"));
                if (optionalCoin.isPresent()) {
                    Coin coinToUpdate = optionalCoin.get();
                    BigDecimal updatedMarketCap = coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("market_cap");
                    BigDecimal priceUpdated = coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price");

                    coinService.update(coinToUpdate, updatedMarketCap, priceUpdated);
                } else {
                Coin newCoin = new Coin();
                    newCoin.setName(coin.getString("name"));
                    newCoin.setSymbol(coin.getString("symbol"));
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    newCoin.setCreatedAt(LocalDateTime.parse(coin.getString("date_added"), formatter));
                    newCoin.setMarketCap(coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("market_cap"));
                    coinService.add(newCoin).getBody();

                    Price price = new Price();
                    price.setPrice(coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price"));
                    LocalDateTime now = LocalDateTime.now();
                    price.setDate(now);

                    Coin secCoin = coinService.loadByName(coin.getString("name")).getBody();
                    price.setCoin(secCoin);
                    newCoin.setPrice(price);

                    coinService.add(newCoin);
                }
            }
        }
        return "redirect:/coin";
    }

    @GetMapping("/add")
    public String addCoinView(Model model, @AuthenticationPrincipal UserDetails userDetails, @RequestParam Long walletId) {
        List<Coin> coins = coinService.getAll().getBody();
        if (coins != null) {
            List<String> coinNames = coins.stream().map(Coin::getName).toList();
            model.addAttribute("coinNames", coinNames);
        }

        model.addAttribute("walletId", walletId);
        return "add-coin-form";
    }
}
