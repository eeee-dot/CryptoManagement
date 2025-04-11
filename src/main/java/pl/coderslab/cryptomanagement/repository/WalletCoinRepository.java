package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.entity.WalletCoin;

import java.util.Optional;

public interface WalletCoinRepository extends JpaRepository<WalletCoin, Long> {
    Optional<WalletCoin> findByWalletAndCoin(Wallet wallet, Coin coin);
}
