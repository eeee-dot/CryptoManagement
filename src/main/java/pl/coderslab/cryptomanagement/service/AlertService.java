package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.entity.Alert;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.AlertRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class AlertService extends GenericService<Alert> {
    private final AlertRepository alertRepository;

    public AlertService(AlertRepository alertRepository, Validator validator) {
        super(alertRepository, validator);
        this.alertRepository = alertRepository;
    }

    public ResponseEntity<Alert> update(Long id, Alert alert) {
        Optional<Alert> alertToUpdate = alertRepository.findById(id);
        if(alertToUpdate.isPresent()){
            User user = alert.getUser();
            if(user != null) {
                alertToUpdate.get().setUser(user);
            }

            BigDecimal price = alert.getPriceTarget();
            if(price != null) {
                alertToUpdate.get().setPriceTarget(price);
            }

            LocalDateTime date = alert.getCreatedAt();
            if(date != null) {
                alertToUpdate.get().setCreatedAt(date);
            }

            Coin coin = alert.getCoin();
            if (coin != null) {
                alertToUpdate.get().setCoin(coin);
            }

            alertToUpdate.get().setStatus(alert.getStatus());

            return ResponseEntity.ok(alertRepository.save(alertToUpdate.get()));
        }

        throw new RuntimeException();
    }
}
