package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.dto.PriceDTO;
import pl.coderslab.cryptomanagement.entity.Price;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.PriceService;

@Controller
@RequestMapping("/price")
public class PriceController extends GenericController<Price> {
    private final PriceService priceService;

    public PriceController(PriceService priceService) {
        super(priceService, Price.class);
        this.priceService = priceService;
    }

    @PatchMapping("/price")
    public ResponseEntity<Price> updatePrice() {
        priceService.update(1L, new PriceDTO());
        return ResponseEntity.ok().build();
    }


}
