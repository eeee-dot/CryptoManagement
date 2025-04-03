package pl.coderslab.cryptomanagement.dto;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class AlertDTO {
    private BigDecimal priceTarget;
    private Boolean status;
    private LocalDateTime createdAt;
    private Long coinId;
    private Long userId;
}
