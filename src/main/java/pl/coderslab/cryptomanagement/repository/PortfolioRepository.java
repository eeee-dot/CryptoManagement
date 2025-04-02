package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Portfolio;;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
}
