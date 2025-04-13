package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.api.CoinMarketCapAPI;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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

    public Boolean reloadCoins() {
        JSONObject jsonResponse = CoinMarketCapAPI.getAPIResponse();
        if (jsonResponse != null) {
            JSONArray data = jsonResponse.getJSONArray("data");
            for (int i = 0; i < data.length(); i++) {
                JSONObject coin = data.getJSONObject(i);

                Optional<Coin> optionalCoin = this.findByNameAndSymbol(coin.getString("name"), coin.getString("symbol"));
                if (optionalCoin.isPresent()) {
                    Coin coinToUpdate = optionalCoin.get();
                    BigDecimal updatedMarketCap = coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("market_cap");
                    BigDecimal priceUpdated = coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price");

                    this.update(coinToUpdate, updatedMarketCap, priceUpdated);
                } else {
                    Coin newCoin = new Coin();
                    newCoin.setName(coin.getString("name"));
                    newCoin.setSymbol(coin.getString("symbol"));
                    DateTimeFormatter formatter = DateTimeFormatter.ISO_DATE_TIME;
                    newCoin.setCreatedAt(LocalDateTime.parse(coin.getString("date_added"), formatter));
                    newCoin.setMarketCap(coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("market_cap"));
                    this.add(newCoin).getBody();

                    Price price = new Price();
                    price.setPrice(coin.getJSONObject("quote").getJSONObject("USD").getBigDecimal("price"));
                    LocalDateTime now = LocalDateTime.now();
                    price.setDate(now);

                    Coin secCoin = this.loadByName(coin.getString("name")).getBody();
                    price.setCoin(secCoin);
                    newCoin.setPrice(price);

                    this.add(newCoin);
                }
            }
            return true;
        }
        return false;
    }
}
