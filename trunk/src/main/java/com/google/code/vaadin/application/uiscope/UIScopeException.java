package com.google.code.vaadin.application.uiscope;

public class UIScopeException extends RuntimeException {

    protected UIScopeException() {
        super();
    }

    protected UIScopeException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }

    protected UIScopeException(String message, Throwable cause) {
        super(message, cause);
    }

    protected UIScopeException(String message) {
        super(message);
    }

    protected UIScopeException(Throwable cause) {
        super(cause);
    }

}
