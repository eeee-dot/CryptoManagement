package pl.coderslab.cryptomanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class WalletCoinDTO {
    private Long coinId;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal amount;
}
