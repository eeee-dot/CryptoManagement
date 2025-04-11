package pl.coderslab.cryptomanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Data
public class WalletDTO {
    private Long walletId;
    private String name;
    private LocalDate createdAt;
    private Long userId;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal balance;

    private List<WalletCoinDTO> coins;
}
