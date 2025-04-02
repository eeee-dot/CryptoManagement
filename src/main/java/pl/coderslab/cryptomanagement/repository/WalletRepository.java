package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Wallet;

public interface WalletRepository extends JpaRepository<Wallet, Long> {}
