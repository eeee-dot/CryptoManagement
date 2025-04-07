package pl.coderslab.cryptomanagement.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.WalletService;
import org.springframework.ui.Model;

import java.util.List;

@Controller
@RequestMapping("/wallet")
public class WalletController extends GenericController<Wallet> {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        super(walletService, Wallet.class);
        this.walletService = walletService;
    }

    @GetMapping()
    public String updateWallet(Model model) {
        List<Wallet> wallets = walletService.getAll().getBody();
        model.addAttribute("wallets", wallets);
        return "wallets";
    }

    @GetMapping("/add")
    public String addWallet() {
        return "addWalletForm";
    }

    @PostMapping("/add")
    public String addWallet(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("balance") double balance,
            Model model) {
        model.addAttribute("message", "Wallet added successfully");
        return "wallets";
    }
}
