package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Price;

public interface PriceRepository extends JpaRepository<Price, Long> {}
