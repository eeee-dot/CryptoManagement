package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.coderslab.cryptomanagement.dto.WalletDTO;
import pl.coderslab.cryptomanagement.entity.*;
import pl.coderslab.cryptomanagement.service.CoinService;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;
import pl.coderslab.cryptomanagement.service.WalletCoinService;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@SessionAttributes("walletId")
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;
    private final CoinService coinService;
    private final WalletCoinService walletCoinService;
    private User user;

    public WalletController(WalletService walletService, UserService userService, CoinService coinService, WalletCoinService walletCoinService) {
        this.walletService = walletService;
        this.userService = userService;
        this.coinService = coinService;
        this.walletCoinService = walletCoinService;
    }

    @GetMapping()
    public String getWallets(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        this.user = userService.getUser(userDetails);

        List<Wallet> wallets = walletService.loadWalletsByUser(user).getBody();
        model.addAttribute("wallets", wallets);

        if (wallets != null) {
            List<BigDecimal> totalValues = walletService.calculateWalletsTotalValues(wallets);
            model.addAttribute("totalValues", totalValues);
        }

        return "wallets";
    }


    @GetMapping("/add")
    public String getWallet() {
        return "add-wallet-form";
    }

    @PostMapping("/add")
    public String addWallet(
            @RequestParam("name") String name,
            @RequestParam("address") String address
    ) {

        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAddress(address);
        wallet.setUser(this.user);

        walletService.add(wallet);

        return "redirect:/wallet";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteWallet(@PathVariable Long id) {
        walletService.delete(id);
        return "wallets";
    }

    @GetMapping("/show")
    public String showWallet(@RequestParam("walletId") Long id, Model model) {
        model.addAttribute("title", "Wallet - " + id);
        model.addAttribute("walletId", id);

        Wallet wallet = walletService.getById(id).getBody();
        List<BigDecimal> values = new ArrayList<>();
        if (wallet != null) {
            List<WalletCoin> walletCoins = wallet.getWalletCoins();
            for (WalletCoin walletCoin : walletCoins) {
                Coin coin = walletCoin.getCoin();
                Price price = coin.getPrice();
                values.add(price.getPrice().multiply(walletCoin.getAmount()));

            }
            model.addAttribute("walletCoins", walletCoins);
            model.addAttribute("values", values);
        }
        return "wallet";
    }

    @PostMapping("/add-coin")
    public String addCoinToWallet(@RequestParam String coinName,
                                  @RequestParam BigDecimal amount,
                                  @SessionAttribute("walletId") Long walletId,
                                  SessionStatus sessionStatus) {
        Wallet wallet = walletService.getById(walletId).getBody();

        if (wallet == null) {
            return "404";
        }

        Coin coin = coinService.loadByName(coinName).getBody();
        if (coin == null) {
            return "404";
        }

        Optional<WalletCoin> optionalWalletCoin = walletCoinService.findByWalletAndCoin(wallet, coin);
        if (optionalWalletCoin.isPresent()) {
            WalletCoin walletCoin = optionalWalletCoin.get();
            walletCoinService.update(walletCoin, amount);
        } else {
            WalletCoin walletCoin = new WalletCoin();
            walletCoin.setWallet(wallet);
            walletCoin.setCoin(coin);
            walletCoin.setAmount(amount);
            walletCoinService.save(walletCoin);

            wallet.getWalletCoins().add(walletCoin);
            walletService.add(wallet);
        }

        sessionStatus.setComplete();
        return "redirect:/wallet";
    }

    @DeleteMapping("/delete-coin")
    public String removeCoinFromWallet(@RequestParam("coinId") Long coinId,
                                       @RequestParam("walletId") Long walletId) {
        Wallet wallet = walletService.getById(walletId).getBody();
        if (wallet == null) {
            return "404";
        }

        Coin coin = coinService.getById(coinId).getBody();
        if (coin == null) {
            return "404";
        }
        Optional<WalletCoin> optionalWalletCoins = wallet.getWalletCoins().stream().filter(walletCoin ->
                walletCoin.getCoin().equals(coin)).findFirst();
        if (optionalWalletCoins.isPresent()) {
            WalletCoin walletCoin = optionalWalletCoins.get();
            WalletDTO walletDTO = new WalletDTO();

            walletDTO.setWalletCoins(wallet.getWalletCoins());
            walletDTO.getWalletCoins().remove(walletCoin);

            walletService.update(walletId, walletDTO);
        }
        return "/wallet";
    }


}
