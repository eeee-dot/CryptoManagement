package pl.coderslab.cryptomanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    @NotEmpty
    @Size(min = 5, max = 50)
    private String username;
    @NotEmpty
    @Email
    private String email;
    @NotEmpty
    @Size(min = 2)
    private String passwordHash;

    @CreationTimestamp
    private LocalDateTime created;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<Alert> alerts;

    @ManyToMany
    @JoinTable(
            name = "user_coins",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "coin_id"))
    private List<Coin> coins;
}
