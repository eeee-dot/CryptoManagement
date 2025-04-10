package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.cryptomanagement.entity.Portfolio;;import java.util.List;

public interface PortfolioRepository extends JpaRepository<Portfolio, Long> {
    @Query("SELECT p FROM Portfolio p WHERE p.user.id = :userId ORDER BY p.createdAt DESC")
    List<Portfolio> findLatestPortfolioByUserId(@Param("userId") Long userId, Pageable pagable);
}
