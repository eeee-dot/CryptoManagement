package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Price;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.CoinRepository;
import pl.coderslab.cryptomanagement.repository.PriceRepository;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class PriceService extends GenericService<Price> {
    private final PriceRepository priceRepository;


    public PriceService(PriceRepository priceRepository, Validator validator, CoinRepository coinRepository) {
        super(priceRepository, validator);
        this.priceRepository = priceRepository;
    }

    public ResponseEntity<Price> update(Long id, BigDecimal price) {
        return priceRepository.findById(id)
                .map(priceToUpdate -> {
                        priceToUpdate.setPrice(price);

                    LocalDateTime current = LocalDateTime.now();
                    priceToUpdate.setDate(current);

                    return ResponseEntity.ok(priceRepository.save(priceToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Optional<Price> loadByCoin(Coin coin){
        return priceRepository.findByCoin(coin);
    }
}
