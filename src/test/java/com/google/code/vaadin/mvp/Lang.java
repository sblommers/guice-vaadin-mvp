/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp;

import com.google.code.vaadin.TextBundle;
import com.google.inject.servlet.SessionScoped;

import java.util.Locale;

/**
 * Lang - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 31.01.13
 */
@SessionScoped
public class Lang implements TextBundle {

	/*===========================================[ STATIC VARIABLES ]=============*/

    public static final Locale EN_US = new Locale("en", "US");
    public static final Locale RU_RU = new Locale("ru", "RU");

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Locale locale = RU_RU;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public String getText(String key, Object... params) {
        return key + "-" + locale.getLanguage();
    }

	/*===========================================[ GETTER/SETTER ]================*/

    public void setLocale(Locale locale) {
        this.locale = locale;
    }
}