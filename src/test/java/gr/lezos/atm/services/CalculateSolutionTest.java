package gr.lezos.atm.services;

import org.junit.Test;

import java.util.Map;

import static gr.lezos.atm.services.AtmService.*;
import static gr.lezos.atm.common.Attributes.*;
import static org.junit.Assert.*;

public class CalculateSolutionTest {

    @Test
    public void theAtmCanDispose() {
        // Given the ATM has bills
        Long twenties = 10L;
        Long fifties = 10L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(20L, twenties, fifties);
        // Then the ATM delivers
        assertNotNull(result);
        assertNotNull(result.get(TWENTIES));
        assertNotNull(result.get(FIFTIES));
        assertTrue(result.get(TWENTIES) == 1L);
        assertTrue(result.get(FIFTIES) == 0L);
        assertTrue(result.get(ERROR) == null || result.get(ERROR) == 0L);
    }

    @Test
    public void theAtmReturnsErrorWhenEmpty() {
        // Given the ATM is empty
        Long twenties = 0L;
        Long fifties = 0L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(20L, twenties, fifties);
        // Then the ATM returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }

    @Test
    public void theAtmHasNoFiftiesCanDispose() {
        // Given the ATM has no 50$ bills
        Long twenties = 10L;
        Long fifties = 0L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(100L, twenties, fifties);
        // Then the ATM delivers only 20$
        assertNotNull(result);
        assertNotNull(result.get(TWENTIES));
        assertNotNull(result.get(FIFTIES));
        assertTrue(result.get(TWENTIES) == 5L);
        assertTrue(result.get(FIFTIES) == 0L);
        assertTrue(result.get(ERROR) == null || result.get(ERROR) == 0L);
    }

    @Test
    public void theAtmHasNoTwentiesCanDispose() {
        // Given the ATM has no 50$ bills
        Long twenties = 0L;
        Long fifties = 10L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(100L, twenties, fifties);
        // Then the ATM delivers only 50$
        assertNotNull(result);
        assertNotNull(result.get(TWENTIES));
        assertNotNull(result.get(FIFTIES));
        assertTrue(result.get(TWENTIES) == 0L);
        assertTrue(result.get(FIFTIES) == 2L);
        assertTrue(result.get(ERROR) == null || result.get(ERROR) == 0L);
    }

    @Test
    public void theAtmNoSolutionReturnsError() {
        // Given the ATM has bills
        Long twenties = 10L;
        Long fifties = 10L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(30L, twenties, fifties);
        // Then the ATM returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }

    @Test
    public void theAtmHasNoTwentiesNoSolutionReturnsError() {
        // Given the ATM has no 20$ bills
        Long twenties = 0L;
        Long fifties = 10L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(120L, twenties, fifties);
        // Then the ATM returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }

    @Test
    public void theAtmHasNoFiftiesNoSolutionReturnsError() {
        // Given the ATM has no 50$ bills
        Long twenties = 10L;
        Long fifties = 0L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(90L, twenties, fifties);
        // Then the ATM returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }

    @Test
    public void theAtmInvalidAmountReturnsError() {
        // Given the ATM has bills
        Long twenties = 10L;
        Long fifties = 10L;
        // When I ask for a disposal
        Map<String, Long> result = calculateSolution(55L, twenties, fifties);
        // Then the ATM returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }
}