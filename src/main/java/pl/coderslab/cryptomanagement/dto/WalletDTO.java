package pl.coderslab.cryptomanagement.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotEmpty;
import lombok.Data;
import org.hibernate.annotations.CreationTimestamp;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class WalletDTO {
    private Long walletId;
    private String name;
    private LocalDate createdAt;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal balance;

}
