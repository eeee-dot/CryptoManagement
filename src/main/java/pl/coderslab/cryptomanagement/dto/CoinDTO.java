package pl.coderslab.cryptomanagement.dto;


import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class CoinDTO {
    private Long coinId;
    private String name;
    private String symbol;
    private String description;
    private LocalDateTime createdAt;
    private Long priceId;

    @DecimalMin(value = "0.0", inclusive = false)
    private BigDecimal marketCap;
}
