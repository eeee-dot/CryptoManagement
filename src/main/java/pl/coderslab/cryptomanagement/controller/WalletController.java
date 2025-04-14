package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import pl.coderslab.cryptomanagement.entity.*;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

@Controller
@SessionAttributes("walletId")
@RequestMapping("/wallet")
public class WalletController {
    private final WalletService walletService;
    private final UserService userService;

    public WalletController(WalletService walletService, UserService userService) {
        this.walletService = walletService;
        this.userService = userService;
    }

    @GetMapping()
    public String getWallets(Model model, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails);

        List<Wallet> wallets = walletService.loadWalletsByUser(user);
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
    public String addWallet(@RequestParam("name") String name, @RequestParam("address") String address, @AuthenticationPrincipal UserDetails userDetails) {
        User user = userService.getUser(userDetails);

        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAddress(address);
        wallet.setUser(user);

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
        Wallet wallet = walletService.showWallet(id, model);
        if (wallet == null) {
            return "404";
        }
        return "wallet";
    }

    @PostMapping("/add-coin")
    public String addCoinToWallet(@RequestParam String coinName,
                                  @RequestParam BigDecimal amount,
                                  @SessionAttribute("walletId") Long walletId,
                                  SessionStatus sessionStatus) {
        boolean result = walletService.addCoinToWallet(coinName, amount, walletId);
        sessionStatus.setComplete();
        return result ? "redirect:/wallet" : "404";
    }

    @DeleteMapping("/delete-coin")
    public String removeCoinFromWallet(@RequestParam("coinId") Long coinId,
                                       @RequestParam("walletId") Long walletId) {
        boolean result = walletService.removeCoinFromWallet(coinId, walletId);
        return result ? "/wallet" : "404";
    }
}