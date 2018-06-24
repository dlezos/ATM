package gr.lezos.atm;

import gr.lezos.atm.common.Attributes;
import gr.lezos.atm.services.AtmService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.Map;
import java.util.Scanner;
import java.util.StringTokenizer;

import static gr.lezos.atm.common.Attributes.ERROR;
import static gr.lezos.atm.common.Attributes.FIFTIES;

public class AtmConsoleApplication {

	public static void main(String[] args) {
		// Initialize the engine
		AtmService atmService = new AtmService();
		// Write usage instructions
		System.out.println("Input two numbers separated by comma (,) to initialize the ATM: 5,10 initializes the ATM with 5x20$ and 10x50$");
		System.out.println("Input a single number to make a withdrawal");
		System.out.println("Input \"exit\" to end the application");
		// Read the input
		Scanner scanIn = new Scanner(System.in);
		String inputString = null;
		do {
			System.out.println("Available ATM amount: " + atmService.getTwenties() + "*20$ + " + atmService.getFifties() + "*50$ = " + atmService.getAvailableAmount() + "$");
			System.out.println("Input your command: ");
			inputString = scanIn.nextLine();
			if (!"exit".equals(inputString)) {
				StringTokenizer tokenizer = new StringTokenizer(inputString, ",");
				try {
					if (tokenizer.countTokens() == 2) {
						atmService.initialize(new Long(tokenizer.nextToken()), new Long(tokenizer.nextToken()));
					} else {
						Map<String, Long> result = atmService.dispense(new Long(tokenizer.nextToken()));
						if (AtmService.hasError(result)) {
							System.out.println("Error Result: " + result.get(ERROR));
						} else {
							System.out.println("Disposed: " + result.get(Attributes.TWENTIES) + "*20$ and " + result.get(FIFTIES) + "*50$");
						}
					}
				} catch (Exception e) {
					System.out.println("Error: " + e.getMessage());
				}
			}
		} while (!"exit".equals(inputString));
		scanIn.close();
	}
}
