package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.entity.WalletCoin;

import java.math.BigDecimal;
import java.util.Optional;

public interface WalletCoinRepository extends JpaRepository<WalletCoin, Long> {
    Optional<WalletCoin> findByWalletAndCoin(Wallet wallet, Coin coin);
    @Query("SELECT COUNT(DISTINCT wc.coin.id) FROM WalletCoin wc WHERE wc.wallet.user.id = :userId")
    BigDecimal findTotalAssetsByUserId(@Param("userId") Long userId);
}
