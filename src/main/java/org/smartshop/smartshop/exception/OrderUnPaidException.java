package org.smartshop.smartshop.exception;

public class OrderUnPaidException extends RuntimeException {
    public OrderUnPaidException(String message) {
        super(message);
    }
}
