package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.UserDTO;
import pl.coderslab.cryptomanagement.entity.Coin;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.UserRepository;

@Service
public class UserService extends GenericService<User> {
    private final UserRepository userRepository;
    
    public UserService (UserRepository userRepository, Validator validator) {
        super(userRepository, validator);
        this.userRepository = userRepository;
    }
    
    public ResponseEntity<User> update(Long id, UserDTO userDTO) {
        return userRepository.findById(id)
                .map(userToUpdate -> {
                    if (userDTO.getUsername() != null) {
                        userToUpdate.setUsername(userDTO.getUsername());
                    }
                    if (userDTO.getEmail() != null) {
                        userToUpdate.setEmail(userDTO.getEmail());
                    }
                    if (userDTO.getPasswordHash() != null) {
                        userToUpdate.setPasswordHash(userDTO.getPasswordHash());
                    }

                    return ResponseEntity.ok(userRepository.save(userToUpdate));
                })
                .orElseThrow(() -> new ResourceNotFoundException(id));
    }
}
