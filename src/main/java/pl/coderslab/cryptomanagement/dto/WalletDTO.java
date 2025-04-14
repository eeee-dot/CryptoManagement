package pl.coderslab.cryptomanagement.dto;

import lombok.Data;
import pl.coderslab.cryptomanagement.entity.WalletCoin;

import java.math.BigDecimal;
import java.util.List;

@Data
public class WalletDTO {
    private BigDecimal balance;
    private List<WalletCoin> walletCoins;
}
