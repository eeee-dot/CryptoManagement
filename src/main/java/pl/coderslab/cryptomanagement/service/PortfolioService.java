package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.PortfolioDTO;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.PortfolioRepository;
import pl.coderslab.cryptomanagement.repository.UserRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;


@Service
public class PortfolioService extends GenericService<Portfolio> {
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;
    private final UserService userService;
    private final WalletService walletService;

    public PortfolioService(PortfolioRepository portfolioRepository, Validator validator, UserRepository userRepository, UserService userService, WalletService walletService) {
        super(portfolioRepository, validator);
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
        this.userService = userService;
        this.walletService = walletService;
    }

    public ResponseEntity<Portfolio> update(Long id, PortfolioDTO portfolioDTO) {
        return portfolioRepository.findById(id)
                .map(portfolioToUpdate -> {
                    if (portfolioDTO.getCreatedAt() != null) {
                        portfolioToUpdate.setCreatedAt(portfolioDTO.getCreatedAt());
                    }
                    if (portfolioDTO.getTotalValue() != null) {
                        portfolioToUpdate.setTotalValue(portfolioDTO.getTotalValue());
                    }
                    if (portfolioDTO.getUpdatedAt() != null) {
                        portfolioToUpdate.setUpdatedAt(portfolioDTO.getUpdatedAt());
                    }
                    if (portfolioDTO.getUserId() != null) {
                        User user = userRepository
                                .findById(portfolioDTO.getUserId())
                                .orElseThrow(() -> new ResourceNotFoundException(portfolioDTO.getUserId()));

                        portfolioToUpdate.setUser(user);
                    }
                    return ResponseEntity.ok(portfolioRepository.save(portfolioToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
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

}
