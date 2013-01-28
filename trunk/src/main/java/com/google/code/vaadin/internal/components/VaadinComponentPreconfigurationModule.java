/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.components;

import com.google.code.vaadin.components.Preconfigured;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import javax.servlet.ServletContext;

/**
 * Adds support for {@link Preconfigured} annotation.
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class VaadinComponentPreconfigurationModule extends AbstractModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private final ServletContext servletContext;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public VaadinComponentPreconfigurationModule(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        // support for @Preconfigured
        bindListener(Matchers.any(), new VaadinComponentsInjectionListener(servletContext));
    }
}