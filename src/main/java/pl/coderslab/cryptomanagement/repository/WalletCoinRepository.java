package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.WalletCoin;

public interface WalletCoinRepository extends JpaRepository<WalletCoin, Long> {
}
