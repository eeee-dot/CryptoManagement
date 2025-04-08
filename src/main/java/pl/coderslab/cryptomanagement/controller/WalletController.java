package pl.coderslab.cryptomanagement.controller;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import pl.coderslab.cryptomanagement.service.WalletService;
import org.springframework.ui.Model;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/wallet")
public class WalletController extends GenericController<Wallet> {
    private final WalletService walletService;
    private final UserRepository userRepository;

    public WalletController(WalletService walletService, UserRepository userRepository) {
        super(walletService, Wallet.class);
        this.walletService = walletService;
        this.userRepository = userRepository;
    }

    @GetMapping()
    public String updateWallet(Model model) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentPrincipalName = authentication.getName();
        Optional<User> user = userRepository.findByUsername(currentPrincipalName);

        if (user.isPresent()) {
            List<Wallet> wallets = walletService.loadWalletsByUser(user.get()).getBody();
            model.addAttribute("wallets", wallets);
        } else {
            return "404";
        }
        return "wallets";
    }

    @GetMapping("/add")
    public String addWallet() {
        return "add-wallet-form";
    }

    @PostMapping("/add")
    public String addWallet(
            @RequestParam("name") String name,
            @RequestParam("address") String address,
            @RequestParam("balance") BigDecimal balance,
            @RequestParam("username") String username,
            Model model) {
        Wallet wallet = new Wallet();
        wallet.setName(name);
        wallet.setAddress(address);
        wallet.setBalance(balance);
        Optional<User> user = userRepository.findByUsername(username);
        if (user.isPresent()) {
            wallet.setUser(user.get());
            walletService.add(wallet);
            model.addAttribute("message", "Wallet added successfully");
            return "wallets";
        } else {
            return "404";
        }
    }
}
