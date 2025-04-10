package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.WalletCoinDTO;
import pl.coderslab.cryptomanagement.dto.WalletDTO;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.entity.WalletCoin;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.CoinRepository;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import pl.coderslab.cryptomanagement.repository.WalletRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService extends GenericService<Wallet> {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;
    private final CoinRepository coinRepository;

    public WalletService(WalletRepository walletRepository, Validator validator, UserRepository userRepository, CoinRepository coinRepository) {
        super(walletRepository, validator);
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
        this.coinRepository = coinRepository;
    }
    
    public ResponseEntity<Wallet> update(Long id, WalletDTO walletDTO) {
        return walletRepository.findById(id)
                .map(walletToUpdate -> {
                    if (walletDTO.getName() != null) {
                        walletToUpdate.setName(walletDTO.getName());
                    }
                    if (walletDTO.getBalance() != null) {
                        walletToUpdate.setBalance(walletDTO.getBalance());
                    }
                    if (walletDTO.getUserId() != null) {
                        User user = userRepository
                                .findById(walletDTO.getUserId())
                                .orElseThrow(() -> new ResourceNotFoundException(walletDTO.getUserId()));

                        walletToUpdate.setUser(user);
                    }
                    if(walletDTO.getCoins() != null) {
                        for(WalletCoinDTO coinDTO : walletDTO.getCoins()) {
                            Coin coin = coinRepository.findById(coinDTO.getCoinId())
                                    .orElseThrow(() -> new ResourceNotFoundException(coinDTO.getCoinId()));

                            for (WalletCoin walletCoin : walletToUpdate.getWalletCoins()) {
                                if(walletCoin.getCoin().equals(coin)) {
                                    walletCoin.setAmount(walletCoin.getAmount().add(coinDTO.getAmount()));
                                    walletToUpdate.getWalletCoins().add(walletCoin);
                                }
                            }
                        }
                    }

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


}
