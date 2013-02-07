/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.application.ui;

import com.google.code.vaadin.mvp.MVPApplicationException;

/**
 * UIProviderException - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 08.02.13
 */
public class UIProviderException extends MVPApplicationException {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final long serialVersionUID = 5917952751053418210L;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public UIProviderException(String message) {
        super(message);
    }

    public UIProviderException(Throwable cause) {
        super(cause);
    }

    public UIProviderException(String message, Throwable cause) {
        super(message, cause);
    }
}
