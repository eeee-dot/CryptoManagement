package pl.coderslab.cryptomanagement.service;

import jakarta.validation.Validator;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pl.coderslab.cryptomanagement.dto.UserDTO;
import pl.coderslab.cryptomanagement.entity.User;
import pl.coderslab.cryptomanagement.entity.UserPrincipal;
import pl.coderslab.cryptomanagement.exception.ResourceNotFoundException;
import pl.coderslab.cryptomanagement.generic.GenericService;
import pl.coderslab.cryptomanagement.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Optional;

@Service
public class UserService extends GenericService<User> implements UserDetailsService {
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

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));

        return new UserPrincipal(user);
    }
}
