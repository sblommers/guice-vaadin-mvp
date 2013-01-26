/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.guice;

import com.google.code.vaadin.internal.servlet.GuiceApplicationServlet;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.name.Names;
import com.google.inject.servlet.ServletModule;
import com.google.inject.servlet.ServletScopes;
import com.vaadin.Application;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * AbstractMVPApplicationModule - TODO: description
 * //TODO extends servletmodule
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public abstract class AbstractMVPApplicationModule extends ServletModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Logger logger;
    protected ServletContext servletContext;
    protected Class<? extends Application> applicationClass;

    /*===========================================[ CONSTRUCTORS ]=================*/

    protected AbstractMVPApplicationModule(ServletContext servletContext) {
        logger = LoggerFactory.getLogger(getClass());
        this.servletContext = servletContext;

        if (servletContext != null) {
            try {
                applicationClass = (Class<? extends Application>) Class.forName(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION));
            } catch (Exception e) {
                throw new MVPApplicationException(String.format("ERROR: Unable to instantiate class of [%s]. " +
                        "Please check your webapp deployment descriptor.", Application.class.getName()), e);
            }
        }
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configureServlets() {
        if (servletContext != null) {
            serve("/*").with(GuiceApplicationServlet.class, extractInitParams(servletContext));
            bind(Class.class).annotatedWith(Names.named(MVPApplicationInitParameters.P_APPLICATION)).toInstance(applicationClass);
            bind(Application.class).to(applicationClass).in(ServletScopes.SESSION);
        } else {
            logger.info("Application running in test environment");
        }

        bindTextBundle();
        bindComponents();
    }

    protected abstract void bindTextBundle();

    protected abstract void bindComponents();

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