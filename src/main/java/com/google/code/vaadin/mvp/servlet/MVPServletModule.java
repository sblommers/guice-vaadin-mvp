/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.mvp.servlet;

import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.vaadin.Application;

/**
* MVPServletModule - TODO: description
*
* @author Alexey Krylov (AleX)
* @since 24.01.13
*/
public class MVPServletModule extends ServletModule {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Class<? extends Application> applicationClass;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public MVPServletModule(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass;
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void configureServlets() {
        serve("/*").with(GuiceApplicationServlet.class);
        bind(Application.class).to(applicationClass).in(ServletScopes.SESSION);
    }
}
