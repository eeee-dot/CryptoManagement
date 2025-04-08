package pl.coderslab.cryptomanagement.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import pl.coderslab.cryptomanagement.dto.AlertDTO;
import pl.coderslab.cryptomanagement.entity.Alert;
import pl.coderslab.cryptomanagement.generic.GenericController;
import pl.coderslab.cryptomanagement.service.AlertService;

@Controller
@RequestMapping("/alert")
public class AlertController extends GenericController<Alert> {
    private final AlertService alertService;

    public AlertController(AlertService alertService) {
        super(alertService, Alert.class);
        this.alertService = alertService;
    }

    @GetMapping()
    public String updateAlert() {
        return "alerts";
    }


}
