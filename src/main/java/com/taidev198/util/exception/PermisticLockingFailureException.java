package com.taidev198.util.exception;

public class PermisticLockingFailureException extends RuntimeException {
    public PermisticLockingFailureException(String message) {
        super(message);
    }
}
