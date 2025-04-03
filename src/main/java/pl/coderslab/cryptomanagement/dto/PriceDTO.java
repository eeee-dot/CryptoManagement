package pl.coderslab.cryptomanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class PriceDTO {

    @DecimalMin(value = "0.0", inclusive = false)
    private Long priceId;
    private BigDecimal price;
    private LocalDateTime date;
    private Long coinId;
}
