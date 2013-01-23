/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

/**
 * GuiceVaadinMVPException - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public class GuiceVaadinMVPException extends RuntimeException {
/*===========================================[ STATIC VARIABLES ]=============*/
/*===========================================[ INSTANCE VARIABLES ]===========*/
/*===========================================[ CONSTRUCTORS ]=================*/
/*===========================================[ CLASS METHODS ]================*/

    public GuiceVaadinMVPException(String message) {
        super(message);
    }

    public GuiceVaadinMVPException(String message, Throwable cause) {
        super(message, cause);
    }

    public GuiceVaadinMVPException(Throwable cause) {
        super(cause);
    }
}
