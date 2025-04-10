package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.PortfolioDTO;
import pl.coderslab.cryptomanagement.entity.Portfolio;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.PortfolioRepository;
import pl.coderslab.cryptomanagement.repository.UserRepository;

import java.util.List;
import java.util.Optional;


@Service
public class PortfolioService extends GenericService<Portfolio> {
    private final PortfolioRepository portfolioRepository;
    private final UserRepository userRepository;

    public PortfolioService(PortfolioRepository portfolioRepository, Validator validator, UserRepository userRepository) {
        super(portfolioRepository, validator);
        this.portfolioRepository = portfolioRepository;
        this.userRepository = userRepository;
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

    public Optional<Portfolio> getLatestPortfolioByUserId(Long userId) {
        Pageable pageable = PageRequest.of(0,1);
        List<Portfolio> portfolios = portfolioRepository.findLatestPortfolioByUserId(userId, pageable);
        return portfolios.isEmpty() ? Optional.empty() : Optional.of(portfolios.get(0));
    }

}
