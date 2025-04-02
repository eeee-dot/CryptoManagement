package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.CoinRepository;

import java.util.Optional;

@Service
public class CoinService extends GenericService<Coin> {
    private final CoinRepository coinRepository;

    public CoinService(CoinRepository coinRepository, Validator validator) {
        super(coinRepository, validator);
        this.coinRepository = coinRepository;
    }

//    public ResponseEntity<Coin> update(Long id, Coin coin) {
//        Optional<Coin> coinToUpdate = coinRepository.findById(id);
//        if(coinToUpdate.isPresent()) {
//            String name = coin.getName();
//            if (name != null) {
//                coinToUpdate.get().setName(name);
//            }
//
//            String symbol = coin.getSymbol();
//            if (symbol != null) {
//
//            }
//            coin.getDescription();
//            coin.getMarketCap();
//            coin.getPrice();
//            coin.getCreatedAt();
//        }
//    }

}
