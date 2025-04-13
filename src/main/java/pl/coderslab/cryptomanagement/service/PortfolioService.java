package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.PortfolioDTO;
import pl.coderslab.cryptomanagement.entity.*;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.PortfolioRepository;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import pl.coderslab.cryptomanagement.repository.WalletCoinRepository;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;


@Service
public class PortfolioService extends GenericService<Portfolio> {
    private final PortfolioRepository portfolioRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final WalletCoinRepository walletCoinRepository;
    private final PriceService priceService;

    public PortfolioService(PortfolioRepository portfolioRepository, Validator validator, UserRepository userRepository, UserService userService, WalletService walletService, WalletCoinRepository walletCoinRepository, PriceService priceService) {
        super(portfolioRepository, validator);
        this.portfolioRepository = portfolioRepository;
        this.userService = userService;
        this.walletService = walletService;
        this.walletCoinRepository = walletCoinRepository;
        this.priceService = priceService;
    }

    public ResponseEntity<Portfolio> update(Long id, BigDecimal totalValue) {
        return portfolioRepository.findById(id).map(portfolioToUpdate -> {
            portfolioToUpdate.setTotalValue(totalValue);

            return ResponseEntity.ok(portfolioRepository.save(portfolioToUpdate));
        }).orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public Optional<Portfolio> getPortfolioByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0, 1);
        List<Portfolio> portfolios = portfolioRepository.findPortfolioByUserId(userId, pageable);
        return portfolios.isEmpty() ? Optional.empty() : Optional.of(portfolios.get(0));
    }

    public Portfolio getPortfolio(UserDetails userDetails) {
        User user = userService.getUser(userDetails);
        Optional<Portfolio> optionalPortfolio = this.getPortfolioByUserId(user.getUserId());
        if (optionalPortfolio.isPresent()) {
            BigDecimal totalValue = this.getTotalValue(user);
            Portfolio portfolio = optionalPortfolio.get();
            portfolio.setTotalValue(totalValue);
            this.update(optionalPortfolio.get().getPortfolioId(), totalValue);
            return optionalPortfolio.get();
        } else {
            Portfolio portfolio = new Portfolio();
            portfolio.setUser(user);
            portfolio.setTotalValue(BigDecimal.valueOf(0));

            this.add(portfolio);
            return portfolio;
        }
    }

    public BigDecimal getTotalValue(User user) {
        List<Wallet> wallets = walletService.loadWalletsByUser(user).getBody();
        if (wallets != null) {
            return walletService.calculateTotalValue(wallets);
        }
        return BigDecimal.valueOf(0);
    }

    public int getTotalAssetsForUser(UserDetails userDetails) {
        User user = userService.getUser(userDetails);
        BigDecimal totalAssets = walletCoinRepository.findTotalAssetsByUserId(user.getUserId());
        return totalAssets != null ? totalAssets.intValue() : 0;
    }

    public String getHighestValueAssetForUser(UserDetails userDetails) {
        User user = userService.getUser(userDetails);
        Coin highestValuedCoin = null;
        BigDecimal highestValue = BigDecimal.ZERO;

        List<Wallet> wallets = walletService.loadWalletsByUser(user).getBody();
        if (wallets != null) {
            List<WalletCoin> walletCoins = wallets.stream().flatMap(wallet -> wallet.getWalletCoins().stream()).toList();
            Map<Coin, BigDecimal> coinTotalValue = new HashMap<>();
            for (WalletCoin walletCoin : walletCoins) {
                Coin coin = walletCoin.getCoin();
                BigDecimal amount = walletCoin.getAmount();
                coinTotalValue.put(coin, coinTotalValue.getOrDefault(coin, BigDecimal.ZERO).add(amount));
            }

            for (Map.Entry<Coin, BigDecimal> entry : coinTotalValue.entrySet()) {
                Coin coin = entry.getKey();
                BigDecimal amount = entry.getValue();
                Optional<Price> price = priceService.loadByCoin(coin);
                if (price.isPresent()) {
                    BigDecimal totalValue = amount.multiply(price.get().getPrice());

                    if (totalValue.compareTo(highestValue) > 0) {
                        highestValue = totalValue;
                        highestValuedCoin = coin;
                    }
                }
            }
        }
        if (highestValuedCoin != null) {
            return highestValuedCoin.getName();
        }
        return null;
    }

}
