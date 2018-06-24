package gr.lezos.atm.common;

import java.util.stream.Stream;

public enum ErrorCodes {
    VALID_AMOUNT(0L, "Valid Amount"),
    ERROR_INVALID_AMOUNT(-1L, "Invalid Amount"),
    ERROR_ATM_AMOUNT_SURPASSED(-2L, "ATM Amount Surpassed"),
    ERROR_AMOUNT_TOO_LOW(-3L, "Requested Amount Too Low"),
    ERROR_NO_AVAILABLE_COMBINATION(-4L, "No Available Combination For This Amount");

    private final Long value;
    private final String message;

    ErrorCodes(Long value, String message) {
        this.value = value;
        this.message = message;
    }

    public static final ErrorCodes errorCode(Long value) {
       return Stream.of(ErrorCodes.values()).filter(c -> c.value == value).findAny().orElse(VALID_AMOUNT);
    }

    public Long getValue() {
        return value;
    }

    public String getMessage() {
        return message;
    }
}
