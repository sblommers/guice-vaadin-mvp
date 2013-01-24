/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.guice;

import com.google.code.vaadin.TextBundle;
import com.google.inject.AbstractModule;

import javax.servlet.ServletContext;

/**
 * AbstractMVPApplicationModule - TODO: description
 *
 * @author Alexey Krylov
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

    @Override
    protected void configure() {
        bindTextBundle(TextBundle.class);
    }

    protected abstract void bindTextBundle(Class<TextBundle> textBundleClass);
}