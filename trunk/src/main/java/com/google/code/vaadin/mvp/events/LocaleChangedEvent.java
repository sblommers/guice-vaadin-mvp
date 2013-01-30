/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp.events;

import java.util.Locale;

/**
 * LocaleChangedEvent - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 31.01.13
 */
public class LocaleChangedEvent {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Locale locale;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public LocaleChangedEvent(Locale locale) {
        this.locale = locale;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    public Locale getLocale() {
        return locale;
    }
}
