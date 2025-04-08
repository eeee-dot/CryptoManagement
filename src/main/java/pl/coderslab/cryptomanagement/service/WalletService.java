package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.WalletDTO;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.Wallet;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import pl.coderslab.cryptomanagement.repository.WalletRepository;

import java.util.List;
import java.util.Optional;

@Service
public class WalletService extends GenericService<Wallet> {
    private final WalletRepository walletRepository;
    private final UserRepository userRepository;

    public WalletService(WalletRepository walletRepository, Validator validator, UserRepository userRepository) {
        super(walletRepository, validator);
        this.walletRepository = walletRepository;
        this.userRepository = userRepository;
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
