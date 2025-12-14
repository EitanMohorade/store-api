package com.store.api.exception;

import com.store.api.exception.handler.GlobalExceptionHandler;
import com.store.api.exception.model.ErrorResponse;
import org.junit.jupiter.api.Test;
import org.springframework.http.ResponseEntity;
import org.springframework.mock.web.MockHttpServletRequest;

import static org.junit.jupiter.api.Assertions.*;

public class GlobalExceptionHandlerTest {

    @Test
    void returns404ForResourceNotFound() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest req = new MockHttpServletRequest("GET", "/api/productos/999");
        ResponseEntity<ErrorResponse> resp = handler.handleNotFound(new ResourceNotFoundException(), req);

        assertEquals(404, resp.getStatusCode().value());
        assertNotNull(resp.getBody());
        assertEquals(ErrorCode.RESOURCE_NOT_FOUND.name(), resp.getBody().getCode());
        assertEquals("/api/productos/999", resp.getBody().getPath());
    }

    @Test
    void returns422ForValidation() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/api/productos");
        ResponseEntity<ErrorResponse> resp = handler.handleValidation(new ValidationException(), req);

        assertEquals(422, resp.getStatusCode().value());
        assertEquals(ErrorCode.VALIDATION_ERROR.name(), resp.getBody().getCode());
    }

    @Test
    void returns409ForDuplicate() {
        GlobalExceptionHandler handler = new GlobalExceptionHandler();
        MockHttpServletRequest req = new MockHttpServletRequest("POST", "/api/productos");
        ResponseEntity<ErrorResponse> resp = handler.handleDuplicate(new DuplicateResourceException(), req);

        assertEquals(409, resp.getStatusCode().value());
        assertEquals(ErrorCode.DUPLICATE_RESOURCE.name(), resp.getBody().getCode());
    }
}
