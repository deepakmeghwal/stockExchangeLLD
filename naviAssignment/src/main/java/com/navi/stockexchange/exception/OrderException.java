package com.navi.stockexchange.exception;

/**
 * OrderException is a specific exception class
 */
public class OrderException extends Exception {
    private static final long serialVersionUID = 685754845614853098L;

    public OrderException(String msg) {
        super(msg);
    }
}