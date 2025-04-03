package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.AlertDTO;
import pl.coderslab.cryptomanagement.dto.CoinDTO;
import pl.coderslab.cryptomanagement.entity.Alert;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Price;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.CoinRepository;
import pl.coderslab.cryptomanagement.repository.PriceRepository;

import java.util.Optional;

@Service
public class CoinService extends GenericService<Coin> {
    private final CoinRepository coinRepository;
    private final PriceRepository priceRepository;

    public CoinService(CoinRepository coinRepository, Validator validator, PriceRepository priceRepository) {
        super(coinRepository, validator);
        this.coinRepository = coinRepository;
        this.priceRepository = priceRepository;
    }

    public ResponseEntity<Coin> update(Long id, CoinDTO coinDTO) {
        return coinRepository.findById(id)
                .map(coinToUpdate -> {
                    if (coinDTO.getName() != null) {
                        coinToUpdate.setName(coinDTO.getName());
                    }
                    if (coinDTO.getSymbol() != null) {
                        coinToUpdate.setSymbol(coinDTO.getSymbol());
                    }
                    if (coinDTO.getDescription() != null) {
                        coinToUpdate.setDescription(coinDTO.getDescription());
                    }
                    if (coinDTO.getCreatedAt() != null) {
                        coinToUpdate.setCreatedAt(coinDTO.getCreatedAt());
                    }
                    if (coinDTO.getMarketCap() != null) {
                        coinToUpdate.setMarketCap(coinDTO.getMarketCap());
                    }
                    if (coinDTO.getPriceId() != null) {
                        Price price = priceRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException(id));
                        coinToUpdate.setPrice(price);
                    }
                    return ResponseEntity.ok(coinRepository.save(coinToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
