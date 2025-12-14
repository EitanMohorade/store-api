package com.store.api.exception;

/**
 * Recurso solicitado no existe.
 */
public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException() {
        super(ErrorCode.RESOURCE_NOT_FOUND.defaultMessage());
    }
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
