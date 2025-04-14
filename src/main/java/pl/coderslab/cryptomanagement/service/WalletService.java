package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.WalletDTO;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.entity.WalletCoin;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.CoinRepository;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import pl.coderslab.cryptomanagement.repository.WalletCoinRepository;
import pl.coderslab.cryptomanagement.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService extends GenericService<Wallet> {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CoinService coinService;

    public WalletService(WalletRepository walletRepository, Validator validator, UserRepository userRepository, CoinService coinService) {
        super(walletRepository, validator);
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.coinService = coinService;
    }

    public ResponseEntity<Wallet> update(Long id, WalletDTO walletUpdateDTO) {
        return walletRepository.findById(id)
                .map(walletToUpdate -> {
                    if (walletUpdateDTO.getBalance() != null) {
                        walletToUpdate.setBalance(walletUpdateDTO.getBalance());
                    }
                    if (walletUpdateDTO.getWalletCoins() != null) {
                        walletToUpdate.setWalletCoins(walletUpdateDTO.getWalletCoins());
                    }
                    System.out.println(walletToUpdate.getWalletCoins());
                    return ResponseEntity.ok(walletRepository.save(walletToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public ResponseEntity<List<Wallet>> loadWalletsByUser(User user) {
        Optional<List<Wallet>> wallet = walletRepository.findByUser(user);
        if (wallet.isPresent()) {
            return ResponseEntity.ok(wallet.get());
        }
        throw new ResourceNotFoundException("Not user found");
    }

    public BigDecimal calculateWalletTotalValue(Wallet wallet) {
        List<WalletCoin> walletCoins = wallet.getWalletCoins();

        if (walletCoins == null) {
            return BigDecimal.ZERO;
        }

        return walletCoins.stream().map(walletCoin -> {
            BigDecimal coinPrice = coinService.getCoinPrice(walletCoin.getCoin());
            return walletCoin.getAmount().multiply(coinPrice);
        }).reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    public List<BigDecimal> calculateWalletsTotalValues(List<Wallet> wallets) {
        return wallets.stream().map(this::calculateWalletTotalValue).collect(Collectors.toList());
    }

    public BigDecimal calculateTotalValue(List<Wallet> wallets) {
        List<BigDecimal> balances = calculateWalletsTotalValues(wallets);
        return balances.stream().reduce(BigDecimal::add).get();
    }

}
