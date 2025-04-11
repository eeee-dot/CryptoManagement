package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.CoinDTO;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.Price;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.entity.WalletCoin;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.CoinRepository;
import pl.coderslab.cryptomanagement.repository.PriceRepository;

import java.math.BigDecimal;
import java.util.Optional;

@Service
public class CoinService extends GenericService<Coin> {
    private final CoinRepository coinRepository;
    private final PriceRepository priceRepository;
    private final PriceService priceService;

    public CoinService(CoinRepository coinRepository, Validator validator, PriceRepository priceRepository, PriceService priceService) {
        super(coinRepository, validator);
        this.coinRepository = coinRepository;
        this.priceRepository = priceRepository;
        this.priceService = priceService;
    }

    public Coin update(Coin coinToUpdate, BigDecimal marketCap, BigDecimal price) {
        Optional<Price> priceToUpdate = priceRepository.findByCoin(coinToUpdate);
        if (priceToUpdate.isPresent()) {
            priceToUpdate.get().setPrice(price);
            priceService.update(priceToUpdate.get().getPriceId(), price);
        }
        coinToUpdate.setMarketCap(marketCap);
        return coinRepository.save(coinToUpdate);
    }

    public Optional<Coin> findByNameAndSymbol(String name, String symbol) {
        return coinRepository.findByNameAndSymbol(name, symbol);
    }

    public ResponseEntity<Coin> loadByName(String name) {
        Optional<Coin> coin = coinRepository.findByName(name);
        if (coin.isPresent()) {
            return ResponseEntity.ok(coin.get());
        }
        throw new ResourceNotFoundException("No coin found");
    }

    public BigDecimal getCoinPrice(Coin coin) {
        Price price = coin.getPrice();
        return price.getPrice();
    }
}
