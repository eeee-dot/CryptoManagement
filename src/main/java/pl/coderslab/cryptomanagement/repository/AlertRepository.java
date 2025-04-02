package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Alert;

public interface AlertRepository extends JpaRepository<Alert, Long> {}
