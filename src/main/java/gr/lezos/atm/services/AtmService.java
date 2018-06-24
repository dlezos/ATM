package gr.lezos.atm.services;


import gr.lezos.atm.common.ErrorCodes;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Service;

import java.util.AbstractMap;
import java.util.Map.Entry;
import java.util.AbstractMap.SimpleImmutableEntry;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static gr.lezos.atm.common.Attributes.*;
import static gr.lezos.atm.common.ErrorCodes.*;


/**
 * The ATM service
 */
@Service
@Scope(value = ConfigurableBeanFactory.SCOPE_SINGLETON)
public class AtmService {
    Logger LOGGER = LoggerFactory.getLogger(AtmService.class);


    /**
     * A list of precalculated combinations (not necessary single solutions)
     */
    private static final HashMap<Long, Map<String, Long>> precalculated = new HashMap<Long, Map<String, Long>>() {{
        put(20L, new HashMap<String, Long>(){{put(TWENTIES, 1L);put(FIFTIES, 0L);}});
        put(40L, new HashMap<String, Long>(){{put(TWENTIES, 2L);put(FIFTIES, 0L);}});
        put(50L, new HashMap<String, Long>(){{put(TWENTIES, 0L);put(FIFTIES, 1L);}});
        put(60L, new HashMap<String, Long>(){{put(TWENTIES, 3L);put(FIFTIES, 0L);}});
        put(70L, new HashMap<String, Long>(){{put(TWENTIES, 1L);put(FIFTIES, 1L);}});
        put(80L, new HashMap<String, Long>(){{put(TWENTIES, 4L);put(FIFTIES, 0L);}});
        put(90L, new HashMap<String, Long>(){{put(TWENTIES, 2L);put(FIFTIES, 1L);}});
        put(100L, new HashMap<String, Long>(){{put(TWENTIES, 0L);put(FIFTIES, 2L);}});
        put(110L, new HashMap<String, Long>(){{put(TWENTIES, 3L);put(FIFTIES, 1L);}});
    }};

    private Long twenties = 0L;
    private Long fifties = 0L;

    /**
     * Initialize the ATM
     * @param twenties # of 20$ bills
     * @param fifties # of 50$ bills
     * @return
     */
    public boolean initialize(Long twenties, Long fifties) {
        this.twenties = twenties;
        this.fifties = fifties;
        return true;
    }

    /**
     * Get the total available amount in the ATM
     * @return the total available amount in the ATM
     */
    public Long getAvailableAmount() {
        return (BILL_20_VALUE * twenties) + (BILL_50_VALUE * fifties);
    }

    /**
     * The # of 50$ bills in the ATM
     * @return The # of 50$ bills in the ATM
     */
    public Long getFifties() {
        return fifties;
    }

    /**
     * The # of 20$ bills in the ATM
     * @return The # of 20$ bills in the ATM
     */
    public Long getTwenties() {
        return twenties;
    }

    /**
     * Dispense the requested amount of $
     * @param amount The amount of $ to dispense
     * @return A Map containing
     */
    public Map<String, Long> dispense(Long amount) {
        Map<String, Long> result = new HashMap<>();
        // Check the amount validity
        ErrorCodes validationResult = validAmount(amount);
        // Check for a valid amount
        if (validationResult == VALID_AMOUNT) {
            result = calculateSolution(amount, twenties, fifties);
            validationResult = errorCode(result.get(ERROR));
        }
        if (validationResult != null && validationResult != VALID_AMOUNT) {
            // Add the validation code as error response
            return result(null, null, validationResult);
        }
        // substract the bills from the available ones
        twenties -= result.get(TWENTIES);
        fifties -= result.get(FIFTIES);
        return result;
    }

    /**
     * Calculate a solution for the amount requested with the most 50$ available
     * @param amount The amount requested, already checked to be a valid amount
     * @param twentiesAvailable The 20$ bills available
     * @param fiftiesAvailable The 50$ bills available
     * @return A solution for the amount requested
     */
    public static final Map<String, Long> calculateSolution(Long amount, Long twentiesAvailable, Long fiftiesAvailable) {
        Map<String, Long> result;
        // Check for a number of pre-calculated amounts
        if (precalculated.containsKey(amount)) {
            result = precalculated.get(amount);
            if (result.get(TWENTIES) <= twentiesAvailable && result.get(FIFTIES) <= fiftiesAvailable) {
                return result;
            }
        }
        // Initialize result
        Long twenties = 0L;
        // Calculate the # of 50$ bills
        Long fifties = amount / BILL_50_VALUE;
        // Check that we do not surpass the available 50$ bills
        if (fifties > fiftiesAvailable) {
            fifties = fiftiesAvailable;
        }
        while(fifties >= 0) {
            // Calculate the # of 20$ bills for the remaining amount
            twenties = (amount - BILL_50_VALUE * fifties) / BILL_20_VALUE;
            // Check that the ATM has the required bills
            if (twenties > twentiesAvailable) {
                return result(null, null, ERROR_NO_AVAILABLE_COMBINATION);
            }
            // Check that we have the requested amount
            if (amount == (fifties * BILL_50_VALUE + twenties * BILL_20_VALUE)) {
                // Use this solution
                return result(twenties, fifties, null);
            }
            // Try to replace a 50$ bill with 20$ bills
            fifties--;
        }
        return result(null, null, ERROR_NO_AVAILABLE_COMBINATION);
    }

    /**
     * Return
     * @param amount
     * @return
     */
    protected ErrorCodes validAmount(Long amount) {
        // Check for an amount that requires coins
        if (amount % 10 != 0) {
            return ERROR_INVALID_AMOUNT;
        }
        // Check for an amount that surpasses the money in the ATM
        if (amount > getAvailableAmount()) {
            return ERROR_ATM_AMOUNT_SURPASSED;
        }
        // Check for an amount above or equal the less the ATM can deliver
        if (amount < BILL_20_VALUE) {
            return ERROR_AMOUNT_TOO_LOW;
        }
        return VALID_AMOUNT;
    }

    protected static final Map<String, Long> result(Long twenties, Long fifties, ErrorCodes error) {
        if (error == null) {
            Map<String, Long> result = new HashMap<>();
            result.put(TWENTIES, twenties);
            result.put(FIFTIES, fifties);
            return result;
        }
        return Collections.singletonMap(ERROR, error.getValue());
    }

    public static final boolean hasError(Map<String, Long> result) {
        return result.get(ERROR) != null;
    }
}
