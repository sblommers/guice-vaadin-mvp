package com.google.code.vaadin.application.uiscope;

import com.google.code.vaadin.mvp.MVPApplicationException;

public class UIScopeException extends MVPApplicationException {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = -1325029302330469640L;

	/*===========================================[ CONSTRUCTORS ]=================*/

    protected UIScopeException(String message) {
        super(message);
    }

    protected UIScopeException(Throwable cause) {
        super(cause);
    }

    protected UIScopeException(String message, Throwable cause) {
        super(message, cause);
    }
}
