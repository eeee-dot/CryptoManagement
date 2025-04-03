package pl.coderslab.cryptomanagement.dto;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.ManyToOne;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import pl.coderslab.cryptomanagement.entity.User;

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
