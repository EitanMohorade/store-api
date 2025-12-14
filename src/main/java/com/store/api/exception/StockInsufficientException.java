package com.store.api.exception;

/**
 * No hay stock suficiente para realizar la operación.
 */
public class StockInsufficientException extends RuntimeException {
    public StockInsufficientException() {
        super(ErrorCode.STOCK_INSUFFICIENT.defaultMessage());
    }
    public StockInsufficientException(String message) {
        super(message);
    }
}
