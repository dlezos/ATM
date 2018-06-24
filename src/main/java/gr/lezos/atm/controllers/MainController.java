package gr.lezos.atm.controllers;

import gr.lezos.atm.common.ErrorCodes;
import gr.lezos.atm.services.AtmService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.Mapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

import static gr.lezos.atm.common.Attributes.ERROR;
import static gr.lezos.atm.common.Attributes.FIFTIES;
import static gr.lezos.atm.common.Attributes.TWENTIES;

@Controller("/")
public class MainController {
    @Autowired
    AtmService atmService;

    @GetMapping
    public String index(Model model) {
        model.addAttribute("atm", "Available ATM amount: " + atmService.getTwenties() + "*20$ + " + atmService.getFifties() + "*50$ = " + atmService.getAvailableAmount() + "$");
        return "index";
    }

    @PostMapping("/initialize")
    public String initialize(@RequestParam("twenties") Long twenties, @RequestParam("fifties") Long fifties, Model model) {
        atmService.initialize(twenties, fifties);
        model.addAttribute("atm", "Available ATM amount: " + atmService.getTwenties() + "*20$ + " + atmService.getFifties() + "*50$ = " + atmService.getAvailableAmount() + "$");
        return "index";
    }

    @RequestMapping("/dispense")
    public String dispense(@RequestParam("amount") Long amount, Model model) {
        Map<String, Long> result = atmService.dispense(amount);
        model.addAttribute("atm", "Available ATM amount: " + atmService.getTwenties() + "*20$ + " + atmService.getFifties() + "*50$ = " + atmService.getAvailableAmount() + "$");
        if (AtmService.hasError(result)) {
            model.addAttribute(ERROR, ErrorCodes.errorCode(result.get(ERROR)).getMessage());
        } else {
            model.addAttribute(TWENTIES, result.get(TWENTIES));
            model.addAttribute(FIFTIES, result.get(FIFTIES));
        }
        return "index";
    }
}
