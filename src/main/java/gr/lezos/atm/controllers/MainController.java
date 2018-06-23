package gr.lezos.atm.controllers;

import gr.lezos.atm.services.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController("/")
public class MainController {
    @Autowired
    AtmService atmService;

    @RequestMapping("/dispense")
    public Map<String, Long> dispense(@RequestParam("twenties") Long twenties, @RequestParam("fifties") Long fifties, @RequestParam("amount") Long amount) {
        atmService.initialize(twenties, fifties);
        Map<String, Long> result = atmService.dispense(amount);
        return result;
    }
}
