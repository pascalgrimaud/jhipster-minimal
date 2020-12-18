package io.github.jhipster.common.domain.error;

public enum ErrorHttpStatus {
    INTERNAL_SERVER_ERROR(500),
    BAD_REQUEST(400);
    private final int statusCode;

    private ErrorHttpStatus(int statusCode) { this.statusCode = statusCode; }

    public int getStatusCode() { return statusCode; }
}
