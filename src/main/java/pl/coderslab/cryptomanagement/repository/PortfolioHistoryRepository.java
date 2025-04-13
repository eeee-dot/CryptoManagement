package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import pl.coderslab.cryptomanagement.entity.PortfolioHistory;

import java.util.List;

public interface PortfolioHistoryRepository extends JpaRepository<PortfolioHistory, Long> {
    @Query("SELECT ph FROM PortfolioHistory ph WHERE ph.portfolio.id = :portfolioId ORDER BY ph.recordedAt DESC")
    List<PortfolioHistory> findTop7ByPortfolioOrderByRecordedAtDesc(@Param("portfolioId") Long portfolioId, Pageable pageable);
}
