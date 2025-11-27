package org.smartshop.smartshop.exception;

public class OrderAlreadyPayed extends RuntimeException {
    public OrderAlreadyPayed(String message) {
        super(message);
    }
}
