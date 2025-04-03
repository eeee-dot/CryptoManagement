package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.PriceDTO;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Price;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.CoinRepository;
import pl.coderslab.cryptomanagement.repository.PriceRepository;

import java.time.LocalDateTime;

@Service
public class PriceService extends GenericService<Price> {
    private final PriceRepository priceRepository;
    private final CoinRepository coinRepository;

    public PriceService(PriceRepository priceRepository, Validator validator, CoinRepository coinRepository) {
        super(priceRepository, validator);
        this.priceRepository = priceRepository;
        this.coinRepository = coinRepository;
    }

    public ResponseEntity<Price> update(Long id, PriceDTO priceDTO) {
        return priceRepository.findById(id)
                .map(priceToUpdate -> {
                    if (priceDTO.getPrice() != null) {
                        priceToUpdate.setPrice(priceDTO.getPrice());
                    }
                    if (priceDTO.getCoinId() != null) {
                        Coin coin = coinRepository
                                .findById(priceDTO.getCoinId())
                                .orElseThrow(() -> new ResourceNotFoundException(priceDTO.getCoinId()));

                        priceToUpdate.setCoin(coin);
                    }
                    LocalDateTime current = LocalDateTime.now();
                    priceToUpdate.setDate(current);

                    return ResponseEntity.ok(priceRepository.save(priceToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
