package pl.coderslab.cryptomanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;
import java.math.BigDecimal;
import java.time.LocalDate;

@Entity
@Data
public class Wallet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long walletId;

    @NotEmpty
    private String name;

    @CreationTimestamp
    private LocalDate createdAt;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal balance;

    @ManyToOne
    private User user;
}
