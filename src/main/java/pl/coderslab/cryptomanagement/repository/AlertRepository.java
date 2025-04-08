package pl.coderslab.cryptomanagement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.coderslab.cryptomanagement.entity.Alert;
import pl.coderslab.cryptomanagement.entity.User;

import java.util.List;
import java.util.Optional;

public interface AlertRepository extends JpaRepository<Alert, Long> {
    Optional<List<Alert>> findByUser(User user);
}
