package com.subscriptionzen.exceptions;

/**
 * Custom exception thrown when a Subscription operation is invalid.
 * Demonstrates: Custom User-Defined Exception, Constructor Overloading.
 */
public class InvalidSubscriptionException extends Exception {

    // ── Constructor with message only ───────────────────────
    public InvalidSubscriptionException(String message) {
        super(message);
    }

    // ── Constructor with message and cause ──────────────────
    public InvalidSubscriptionException(String message, Throwable cause) {
        super(message, cause);
    }
}
