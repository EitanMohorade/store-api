package com.store.api.exception;

/**
 * Intento de crear un recurso que ya existe.
 */
public class DuplicateResourceException extends RuntimeException {
    public DuplicateResourceException() {
        super(ErrorCode.DUPLICATE_RESOURCE.defaultMessage());
    }
    public DuplicateResourceException(String message) {
        super(message);
    }
}