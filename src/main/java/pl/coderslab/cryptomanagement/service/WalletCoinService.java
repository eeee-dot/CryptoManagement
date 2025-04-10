package pl.coderslab.cryptomanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.entity.WalletCoin;
import pl.coderslab.cryptomanagement.repository.WalletCoinRepository;

@Service
@AllArgsConstructor
public class WalletCoinService {
    private final WalletCoinRepository walletCoinRepository;

    public WalletCoin save(WalletCoin walletCoin) {
        return walletCoinRepository.save(walletCoin);
    }
}
