package pl.coderslab.cryptomanagement.service;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.entity.WalletCoin;
import pl.coderslab.cryptomanagement.repository.WalletCoinRepository;

import java.math.BigDecimal;
import java.util.Optional;


@Service
@AllArgsConstructor
public class WalletCoinService {
    private final WalletCoinRepository walletCoinRepository;

    public WalletCoin save(WalletCoin walletCoin) {
        return walletCoinRepository.save(walletCoin);
    }

    public WalletCoin update(WalletCoin walletCoinToUpdate, BigDecimal amount) {
        walletCoinToUpdate.setAmount(walletCoinToUpdate.getAmount().add(amount));
        return walletCoinRepository.save(walletCoinToUpdate);
    }

    public Optional<WalletCoin> findByWalletAndCoin(Wallet wallet, Coin coin) {
        return walletCoinRepository.findByWalletAndCoin(wallet, coin);
    }
}
