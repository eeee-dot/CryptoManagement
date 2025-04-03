package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.AlertDTO;
import pl.coderslab.cryptomanagement.entity.Alert;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.AlertRepository;
import pl.coderslab.cryptomanagement.repository.CoinRepository;
import pl.coderslab.cryptomanagement.repository.UserRepository;

@Service
public class AlertService extends GenericService<Alert> {
    private final AlertRepository alertRepository;
    private final CoinRepository coinRepository;
    private final UserRepository userRepository;

    public AlertService(AlertRepository alertRepository, Validator validator, CoinRepository coinRepository, UserRepository userRepository) {
        super(alertRepository, validator);
        this.alertRepository = alertRepository;
        this.coinRepository = coinRepository;
        this.userRepository = userRepository;
    }

    public ResponseEntity<Alert> update(Long id, AlertDTO alertDTO) {
        return alertRepository.findById(id)
                .map(alertToUpdate -> {
                    if(alertDTO.getPriceTarget() != null) {
                        alertToUpdate.setPriceTarget(alertDTO.getPriceTarget());
                    }
                    if(alertDTO.getCreatedAt() != null) {
                        alertToUpdate.setCreatedAt(alertDTO.getCreatedAt());
                    }
                    if(alertDTO.getStatus() != null) {
                        alertToUpdate.setStatus(alertDTO.getStatus());
                    }
                    if(alertDTO.getCoinId() != null) {
                        Coin coin = coinRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
                        alertToUpdate.setCoin(coin);
                    }
                    if (alertDTO.getUserId() != null) {
                        User user = userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
                        alertToUpdate.setUser(user);
                    }
                    return ResponseEntity.ok(alertRepository.save(alertToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
