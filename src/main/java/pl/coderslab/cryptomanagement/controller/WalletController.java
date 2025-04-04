package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.dto.WalletDTO;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.WalletService;

@Controller
@RequestMapping("/wallet")
public class WalletController extends GenericController<Wallet> {
    private final WalletService walletService;

    public WalletController(WalletService walletService) {
        super(walletService, Wallet.class);
        this.walletService = walletService;
    }

    @PatchMapping("/wallet")
    public ResponseEntity<Wallet> updateWallet() {
        walletService.update(1L, new WalletDTO());
        return ResponseEntity.ok().build();
    }


}
