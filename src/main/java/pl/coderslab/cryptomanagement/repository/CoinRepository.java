package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Coin;

public interface CoinRepository extends JpaRepository<Coin, Long> {}
