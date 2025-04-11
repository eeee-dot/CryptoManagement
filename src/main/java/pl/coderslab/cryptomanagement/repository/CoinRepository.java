package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Coin;

import java.util.Optional;

public interface CoinRepository extends JpaRepository<Coin, Long> {
    Optional<Coin> findByName(String name);

    Optional<Coin> findByNameAndSymbol(String name, String symbol);
}
