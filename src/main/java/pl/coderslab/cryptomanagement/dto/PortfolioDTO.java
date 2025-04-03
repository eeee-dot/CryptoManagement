package pl.coderslab.cryptomanagement.dto;

import jakarta.validation.constraints.DecimalMin;
import lombok.Data;


import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PortfolioDTO {

    @DecimalMin(value = "0.0", inclusive = false)
    private Long portfolioId;
    private BigDecimal totalValue;
    private LocalDate createdAt;
    private LocalDateTime updatedAt;
    private Long userId;
}
