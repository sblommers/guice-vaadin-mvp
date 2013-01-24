/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.servlet;

import com.google.code.vaadin.guice.MVPApplicationContextListener;
import com.google.code.vaadin.mvp.RequestContext;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.vaadin.Application;

/**
 * MVPServletModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class MVPApplicationServletModule extends ServletModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Class<? extends Application> applicationClass;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public MVPApplicationServletModule(Class<? extends Application> applicationClass) {
        this.applicationClass = applicationClass;
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void configureServlets() {
        serve("/*").with(GuiceApplicationServlet.class);
        bind(RequestContext.class).in(RequestScoped.class);
        bind(Class.class).annotatedWith(Names.named(MVPApplicationContextListener.P_APPLICATION)).toInstance(applicationClass);
        bind(Application.class).to(applicationClass).in(ServletScopes.SESSION);
    }
}