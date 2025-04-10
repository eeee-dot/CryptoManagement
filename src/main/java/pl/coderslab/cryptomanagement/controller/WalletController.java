package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.service.UserService;
import pl.coderslab.cryptomanagement.service.WalletService;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;

@Controller
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
            @RequestParam("balance") BigDecimal balance,
            @AuthenticationPrincipal UserDetails userDetails
           ) {
        User user = userService.getUser(userDetails);

        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAddress(address);
        wallet.setBalance(balance);
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
                                  @RequestParam String walletName) {

        return "redirect:/wallets";
    }
}
