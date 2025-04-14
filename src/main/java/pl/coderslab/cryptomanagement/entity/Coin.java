package pl.coderslab.cryptomanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Coin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long coinId;

    @NotEmpty
    private String name;

    @NotEmpty
    private String symbol;

    private LocalDateTime createdAt;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal marketCap;

    @OneToOne(mappedBy = "coin", cascade = CascadeType.ALL)
    @ToString.Exclude
    private Price price;

    @ManyToMany(mappedBy = "coins")
    @ToString.Exclude
    private List<User> users;

   @OneToMany(mappedBy = "coin", cascade = CascadeType.ALL)
   @ToString.Exclude
    private List<WalletCoin> walletCoins;

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Coin coin = (Coin) o;
        return Objects.equals(coinId, coin.coinId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(coinId);
    }
}
