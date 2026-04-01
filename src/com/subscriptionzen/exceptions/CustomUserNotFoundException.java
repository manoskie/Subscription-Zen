package com.subscriptionzen.exceptions;

/**
 * Custom exception thrown when a User is not found in the system.
 * Demonstrates: Custom User-Defined Exception, Constructor Overloading.
 */
public class CustomUserNotFoundException extends Exception {

    // ── Constructor with message only ───────────────────────
    public CustomUserNotFoundException(String message) {
        super(message);
    }

    // ── Constructor with message and cause ──────────────────
    public CustomUserNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
