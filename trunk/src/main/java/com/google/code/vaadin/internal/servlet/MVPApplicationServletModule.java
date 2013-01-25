/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.servlet;

import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.code.vaadin.mvp.RequestContext;
import com.google.inject.name.Names;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.vaadin.Application;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * MVPServletModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class MVPApplicationServletModule extends ServletModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Class<? extends Application> applicationClass;
    protected ServletContext servletContext;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public MVPApplicationServletModule(ServletContext servletContext) {
        this.servletContext = servletContext;
        try {
            applicationClass = (Class<? extends Application>) Class.forName(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION));
        } catch (Exception e) {
            throw new MVPApplicationException(String.format("ERROR: Unable to instantiate class of [%s]. " +
                    "Please check your webapp deployment descriptor.", Application.class.getName()), e);
        }
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void configureServlets() {
        serve("/*").with(GuiceApplicationServlet.class, extractInitParams(servletContext));
        bind(RequestContext.class).in(RequestScoped.class);
        bind(Class.class).annotatedWith(Names.named(MVPApplicationInitParameters.P_APPLICATION)).toInstance(applicationClass);
        bind(Application.class).to(applicationClass).in(ServletScopes.SESSION);
    }

    protected Map<String, String> extractInitParams(ServletContext servletContext) {
        Map<String, String> initParams = new HashMap<String, String>();
        Enumeration parameterNames = servletContext.getInitParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = parameterNames.nextElement().toString();
            String value = servletContext.getInitParameter(name);
            initParams.put(name, value);
        }

        return initParams;
    }
}