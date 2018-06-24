package gr.lezos.atm.services;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Map;

import static gr.lezos.atm.common.Attributes.*;
import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AtmServiceTest {

    @Autowired
    AtmService atmService;

    @Test
    public void theAtmCanDispose() {
        // Given the ATM has bills
        atmService.initialize(10L, 10L);
        // When I ask for a disposal
        Map<String, Long> result = atmService.dispense(120L);
        // Then the ATM delivers
        assertNotNull(result);
        assertNotNull(result.get(TWENTIES));
        assertNotNull(result.get(FIFTIES));
        assertTrue(result.get(TWENTIES) == 1L);
        assertTrue(result.get(FIFTIES) == 2L);
        assertTrue(result.get(ERROR) == null || result.get(ERROR) == 0L);
    }

    @Test
    public void theAtmConsecutiveCallsDelivers() {
        // Given the ATM has bills
        atmService.initialize(10L, 10L);
        // When I make consecutive disposal calls
        Map<String, Long> result1 = atmService.dispense(50L);
        Map<String, Long> result2 = atmService.dispense(150L);
        Map<String, Long> result3 = atmService.dispense(120L);
        Map<String, Long> result4 = atmService.dispense(180L);
        // Then the ATM delivers
        assertNotNull(result1);
        assertNotNull(result1.get(TWENTIES));
        assertNotNull(result1.get(FIFTIES));
        assertTrue(result1.get(TWENTIES) == 0L);
        assertTrue(result1.get(FIFTIES) == 1L);
        assertTrue(result1.get(ERROR) == null || result1.get(ERROR) == 0L);
        //
        assertNotNull(result2);
        assertNotNull(result2.get(TWENTIES));
        assertNotNull(result2.get(FIFTIES));
        assertTrue(result2.get(TWENTIES) == 0L);
        assertTrue(result2.get(FIFTIES) == 3L);
        assertTrue(result2.get(ERROR) == null || result2.get(ERROR) == 0L);
        //
        assertNotNull(result3);
        assertNotNull(result3.get(TWENTIES));
        assertNotNull(result3.get(FIFTIES));
        assertTrue(result3.get(TWENTIES) == 1L);
        assertTrue(result3.get(FIFTIES) == 2L);
        assertTrue(result3.get(ERROR) == null || result3.get(ERROR) == 0L);
        //
        assertNotNull(result4);
        assertNotNull(result4.get(TWENTIES));
        assertNotNull(result4.get(FIFTIES));
        assertTrue(result4.get(TWENTIES) == 4L);
        assertTrue(result4.get(FIFTIES) == 2L);
        assertTrue(result4.get(ERROR) == null || result4.get(ERROR) == 0L);
    }

    @Test
    public void theAtmConsecutiveCallsUntilEmptyError() {
        // Given the ATM has bills (total of 700$)
        atmService.initialize(10L, 10L);
        // When I make consecutive disposal calls up to 800$
        Map<String, Long> result = null;
        for (int i=0; i <=7; i++) {
            result = atmService.dispense(100L);
        }
        // Then the ATM empties and returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }

    @Test
    public void theAtmInvalidAmount101ReturnError() {
        // Given the ATM has bills (total of 700$)
        atmService.initialize(10L, 10L);
        // When I ask for an invalid amount
        Map<String, Long> result = result = atmService.dispense(101L);
        // Then the ATM empties and returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }

    @Test
    public void theAtmInvalidAmountMinus20ReturnError() {
        // Given the ATM has bills (total of 700$)
        atmService.initialize(10L, 10L);
        // When I ask for an invalid amount
        Map<String, Long> result = result = atmService.dispense(-20L);
        // Then the ATM empties and returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }

    @Test
    public void theAtmInvalidAmount10ReturnError() {
        // Given the ATM has bills (total of 700$)
        atmService.initialize(10L, 10L);
        // When I ask for an invalid amount
        Map<String, Long> result = result = atmService.dispense(10L);
        // Then the ATM empties and returns error
        assertTrue(result.get(ERROR) != null && result.get(ERROR) != 0L);
    }
}