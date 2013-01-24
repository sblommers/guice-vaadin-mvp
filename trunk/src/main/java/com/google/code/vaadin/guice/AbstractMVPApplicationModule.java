/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.guice;

import com.google.code.vaadin.TextBundle;
import com.google.code.vaadin.components.VaadinComponentsInjectionListener;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

import javax.servlet.ServletContext;

/**
 * AbstractMVPApplicationModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.01.13
 */
public abstract class AbstractMVPApplicationModule extends AbstractModule {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    protected ServletContext servletContext;

	/*===========================================[ CONSTRUCTORS ]=================*/

    protected AbstractMVPApplicationModule(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    //TODO:
    @Override
    protected void configure() {
        bindTextBundle(TextBundle.class);
        // support for @Preconfigured
        bindListener(Matchers.any(), new VaadinComponentsInjectionListener());
    }

    protected abstract void bindTextBundle(Class<TextBundle> textBundleClass);
}