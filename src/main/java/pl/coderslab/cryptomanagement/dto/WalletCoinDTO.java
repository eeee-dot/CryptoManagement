package pl.coderslab.cryptomanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.coderslab.cryptomanagement.entity.Coin;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class WalletCoinDTO {
    private Coin coin;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}
