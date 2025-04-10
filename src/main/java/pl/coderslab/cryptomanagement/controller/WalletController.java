package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.entity.WalletCoin;
import pl.coderslab.cryptomanagement.service.CoinService;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;
import pl.coderslab.cryptomanagement.service.WalletCoinService;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

@Controller
@SessionAttributes("walletId")
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;
    private final CoinService coinService;
    private final WalletCoinService walletCoinService;

    public WalletController(WalletService walletService, UserService userService, CoinService coinService, WalletCoinService walletCoinService) {
        this.walletService = walletService;
        this.userService = userService;
        this.coinService = coinService;
        this.walletCoinService = walletCoinService;
    }

    @GetMapping()
    public String getWallets(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails);

        List<Wallet> wallets = walletService.loadWalletsByUser(user).getBody();
        model.addAttribute("wallets", wallets);
        return "wallets";
    }

    @GetMapping("/add")
    public String getWallet() {
        return "add-wallet-form";
    }

    @PostMapping("/add")
    public String addWallet(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @AuthenticationPrincipal UserDetails userDetails
    ) {
        User user = userService.getUser(userDetails);

        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAddress(address);
        wallet.setBalance(BigDecimal.valueOf(0));
        wallet.setUser(user);

        walletService.add(wallet);

        return "redirect:/wallet";
    }

    @DeleteMapping("/delete/{id}")
    public String deleteWallet(@PathVariable Long id) {
        walletService.delete(id);
        return "wallets";
    }

    @PostMapping("/add-coin")
    public String addCoinToWallet(@RequestParam String coinName,
                                  @RequestParam BigDecimal amount,
                                  @SessionAttribute("walletId") Long walletId,
                                  SessionStatus sessionStatus) {
        System.out.println("WalletController: " + walletId);
        Wallet wallet = walletService.getById(Long.valueOf(walletId)).getBody();

        if (wallet == null) {
            return "404";
        }

        Coin coin = coinService.loadByName(coinName).getBody();
        if (coin == null) {
            return "404";
        }

        List<WalletCoin> walletCoins = wallet.getWalletCoins();
        for (WalletCoin walletCoin : walletCoins) {
            if (walletCoin.getCoin().equals(coin)) {
                walletCoin.setAmount(walletCoin.getAmount().add(amount));
                walletCoinService.save(walletCoin);
            }
        }

        WalletCoin walletCoin = new WalletCoin();
        walletCoin.setWallet(wallet);
        walletCoin.setCoin(coin);
        walletCoin.setAmount(amount);
        walletCoinService.save(walletCoin);

        walletCoins.add(walletCoin);

        walletService.add(wallet);

        sessionStatus.setComplete();
        return "redirect:/wallet";
    }
}
