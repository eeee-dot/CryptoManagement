package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.ui.Model;
import pl.coderslab.cryptomanagement.dto.WalletDTO;
import pl.coderslab.cryptomanagement.entity.*;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import pl.coderslab.cryptomanagement.repository.WalletRepository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class WalletService extends GenericService<Wallet> {
    private final WalletRepository walletRepository;
    private final CoinService coinService;
    private final WalletCoinService walletCoinService;

    public WalletService(WalletRepository walletRepository, Validator validator, UserRepository userRepository, CoinService coinService, WalletCoinService walletCoinService) {
        super(walletRepository, validator);
        this.walletRepository = walletRepository;
        this.coinService = coinService;
        this.walletCoinService = walletCoinService;
    }

    public ResponseEntity<Wallet> update(Long id, WalletDTO walletUpdateDTO) {
        return walletRepository.findById(id)
                .map(walletToUpdate -> {
                    if (walletUpdateDTO.getWalletCoins() != null) {
                        walletToUpdate.setWalletCoins(walletUpdateDTO.getWalletCoins());
                    }
                    return ResponseEntity.ok(walletRepository.save(walletToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }

    public List<Wallet> loadWalletsByUser(User user) {
        return walletRepository.findByUser(user).orElseThrow(() -> new ResourceNotFoundException("Not user found"));
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
        return balances.stream().reduce(BigDecimal::add).orElse(BigDecimal.ZERO);
    }

    public Wallet showWallet(Long id, Model model) {
        Wallet wallet = getById(id).getBody();
        if (wallet != null) {
            model.addAttribute("title", "Wallet - " + id);
            model.addAttribute("walletId", id);

            List<BigDecimal> values = wallet.getWalletCoins().stream()
                    .map(walletCoin -> walletCoin.getCoin().getPrice().getPrice().multiply(walletCoin.getAmount()))
                    .collect(Collectors.toList());

            model.addAttribute("walletCoins", wallet.getWalletCoins());
            model.addAttribute("values", values);
        }
        return wallet;
    }

    public boolean addCoinToWallet(String coinName, BigDecimal amount, Long walletId) {
        Wallet wallet = getById(walletId).getBody();
        if (wallet == null) {
            return false;
        }

        Coin coin = coinService.loadByName(coinName).getBody();
        if (coin == null) {
            return false;
        }

        Optional<WalletCoin> optionalWalletCoin = walletCoinService.findByWalletAndCoin(wallet, coin);
        if (optionalWalletCoin.isPresent()) {
            WalletCoin walletCoin = optionalWalletCoin.get();
            walletCoinService.update(walletCoin, amount);
        } else {
            WalletCoin walletCoin = new WalletCoin();
            walletCoin.setWallet(wallet);
            walletCoin.setCoin(coin);
            walletCoin.setAmount(amount);
            walletCoinService.save(walletCoin);

            wallet.getWalletCoins().add(walletCoin);
            add(wallet);
        }
        return true;
    }

    public boolean removeCoinFromWallet(Long coinId, Long walletId) {
        Wallet wallet = getById(walletId).getBody();
        if (wallet == null) {
            return false;
        }

        Coin coin = coinService.getById(coinId).getBody();
        if (coin == null) {
            return false;
        }

        Optional<WalletCoin> optionalWalletCoin = wallet.getWalletCoins().stream()
                .filter(walletCoin -> walletCoin.getCoin().equals(coin))
                .findFirst();

        if (optionalWalletCoin.isPresent()) {
            WalletCoin walletCoin = optionalWalletCoin.get();
            wallet.getWalletCoins().remove(walletCoin);
            walletCoinService.delete(walletCoin.getId());
            add(wallet);
            return true;
        }
        return false;
    }
}