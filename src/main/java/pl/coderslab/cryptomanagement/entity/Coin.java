package pl.coderslab.cryptomanagement.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDateTime;

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

    @NotEmpty
    private String description;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal marketCap;

    @OneToOne(mappedBy = "coin", cascade = CascadeType.ALL)
    private Price price;
}
