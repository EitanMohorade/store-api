package com.store.api.exception.model;

import java.time.Instant;

/**
 * Respuesta estándar de error para exponer en la API.
 */
public class ErrorResponse {
    private Instant timestamp;
    private int status;
    private String error;
    private String code;
    private String message;
    private String path;

    public ErrorResponse() {}

    /**
     * Constructor completo.
     * @param timestamp Marca de tiempo del error
     * @param status Código HTTP
     * @param error Descripción del código HTTP
     * @param code Código de error específico de la aplicación
     * @param message Mensaje de error detallado
     * @param path Ruta del endpoint que generó el error
     * 
     */
    public ErrorResponse(Instant timestamp, int status, String error, String code, String message, String path) {
        this.timestamp = timestamp;
        this.status = status;
        this.error = error;
        this.code = code;
        this.message = message;
        this.path = path;
    }

    public Instant getTimestamp() { return timestamp; }
    public void setTimestamp(Instant timestamp) { this.timestamp = timestamp; }
    public int getStatus() { return status; }
    public void setStatus(int status) { this.status = status; }
    public String getError() { return error; }
    public void setError(String error) { this.error = error; }
    public String getCode() { return code; }
    public void setCode(String code) { this.code = code; }
    public String getMessage() { return message; }
    public void setMessage(String message) { this.message = message; }
    public String getPath() { return path; }
    public void setPath(String path) { this.path = path; }
}
