package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.entity.*;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.*;

import java.math.BigDecimal;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

@Service
public class PortfolioService extends GenericService<Portfolio> {
    private final PortfolioRepository portfolioRepository;
    private final UserService userService;
    private final WalletService walletService;
    private final WalletCoinRepository walletCoinRepository;
    private final PriceService priceService;
    private final PortfolioHistoryRepository portfolioHistoryRepository;
    private final CoinService coinService;

    public PortfolioService(PortfolioRepository portfolioRepository, Validator validator, UserRepository userRepository, UserService userService, WalletService walletService, WalletCoinRepository walletCoinRepository, PriceService priceService, PortfolioHistoryRepository portfolioHistoryRepository, CoinService coinService) {
        super(portfolioRepository, validator);
        this.portfolioRepository = portfolioRepository;
        this.userService = userService;
        this.walletService = walletService;
        this.walletCoinRepository = walletCoinRepository;
        this.priceService = priceService;
        this.portfolioHistoryRepository = portfolioHistoryRepository;
        this.coinService = coinService;
    }

    private void createPortfolioHistory(Portfolio portfolio) {
        PortfolioHistory portfolioHistory = new PortfolioHistory();
        portfolioHistory.setPortfolio(portfolio);
        portfolioHistory.setValue(portfolio.getTotalValue());
        portfolioHistory.setRecordedAt(LocalDateTime.now());
        portfolioHistoryRepository.save(portfolioHistory);
    }

    private boolean needsUpdate(LocalDateTime lastUpdated) {
        return lastUpdated == null || Duration.between(lastUpdated, LocalDateTime.now()).toHours() > 12;
    }

    private void updatePortfolioHistory(Portfolio portfolio, BigDecimal totalValue) {
        Pageable pageable = PageRequest.of(0, 1);
        List<PortfolioHistory> portfolioHistories = portfolioHistoryRepository.findTopByPortfolioOrderByRecordedAtDesc(portfolio.getPortfolioId(), pageable);
        PortfolioHistory latest = portfolioHistories.get(0);
        latest.setValue(totalValue);
        portfolioHistoryRepository.save(latest);
    }

    public ResponseEntity<Portfolio> update(Long id, BigDecimal totalValue) {
        return portfolioRepository.findById(id).map(portfolioToUpdate -> {
            if (needsUpdate(portfolioToUpdate.getUpdatedAt())) {
                createPortfolioHistory(portfolioToUpdate);
                coinService.reloadCoins();
                portfolioToUpdate.setUpdatedAt(LocalDateTime.now());
            }
            portfolioToUpdate.setTotalValue(totalValue);
            updatePortfolioHistory(portfolioToUpdate, totalValue);
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
            BigDecimal totalValue = this.getPortfolioValue(user);
            Portfolio portfolio = optionalPortfolio.get();
            portfolio.setTotalValue(totalValue);
            this.update(portfolio.getPortfolioId(), totalValue);
            return portfolio;
        } else {
            return createNewPortfolio(user);
        }
    }

    private Portfolio createNewPortfolio(User user) {
        Portfolio portfolio = new Portfolio();
        portfolio.setUser(user);
        portfolio.setTotalValue(BigDecimal.valueOf(0));
        this.add(portfolio);
        return portfolio;
    }

    public BigDecimal getPortfolioValue(User user) {
        List<Wallet> wallets = walletService.loadWalletsByUser(user).getBody();
        return wallets != null && !wallets.isEmpty() ? walletService.calculateTotalValue(wallets) : BigDecimal.valueOf(0);
    }

    public int getTotalAssetsForUser(UserDetails userDetails) {
        User user = userService.getUser(userDetails);
        BigDecimal totalAssets = walletCoinRepository.findTotalAssetsByUserId(user.getUserId());
        return totalAssets != null ? totalAssets.intValue() : 0;
    }

    public String getHighestValueAssetForUser(UserDetails userDetails) {
        User user = userService.getUser(userDetails);
        List<Wallet> wallets = walletService.loadWalletsByUser(user).getBody();
        if (wallets == null) return null;

        Map<Coin, BigDecimal> coinTotalValue = calculateCoinTotalValue(wallets);
        return getHighestValuedCoinName(coinTotalValue);
    }

    private Map<Coin, BigDecimal> calculateCoinTotalValue(List<Wallet> wallets) {
        Map<Coin, BigDecimal> coinTotalValue = new HashMap<>();
        wallets.stream()
                .flatMap(wallet -> wallet.getWalletCoins().stream())
                .forEach(walletCoin -> {
                    Coin coin = walletCoin.getCoin();
                    BigDecimal amount = walletCoin.getAmount();
                    coinTotalValue.put(coin, coinTotalValue.getOrDefault(coin, BigDecimal.ZERO).add(amount));
                });
        return coinTotalValue;
    }

    private String getHighestValuedCoinName(Map<Coin, BigDecimal> coinTotalValue) {
        BigDecimal highestValue = BigDecimal.ZERO;
        Coin highestValuedCoin = null;

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
        return highestValuedCoin != null ? highestValuedCoin.getName() : null;
    }

    public List<PortfolioHistory> getLatestEntries(Long portfolioId, int days) {
        Pageable pageable = PageRequest.of(0, days);
        List<PortfolioHistory> allEntries = portfolioHistoryRepository.findTopByPortfolioOrderByRecordedAtDesc(portfolioId, pageable);
        return allEntries.subList(0, Math.min(days, allEntries.size()));
    }
}