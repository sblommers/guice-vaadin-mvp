/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.junit;

import com.google.code.vaadin.guice.AbstractMVPApplicationModule;
import com.google.code.vaadin.internal.components.VaadinComponentPreconfigurationModule;
import com.google.code.vaadin.internal.event.EventBusModule;
import com.google.code.vaadin.internal.logging.LoggerModule;
import com.google.code.vaadin.internal.mapping.PresenterMapperModule;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.inject.Injector;
import com.vaadin.Application;

import javax.servlet.ServletContext;
import java.lang.reflect.*;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * TestMVPApplicationModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public abstract class AbstractMVPApplicationTestModule extends AbstractMVPApplicationModule {

	/*===========================================[ CONSTRUCTORS ]=================*/

    protected AbstractMVPApplicationTestModule(Class<? extends Application> applicationClass) {
        super(createMockedServletContext(applicationClass));
    }

    protected static ServletContext createMockedServletContext(Class<? extends Application> applicationClass) {
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION)).thenReturn(applicationClass.getName());
        when(servletContext.getInitParameterNames()).thenReturn(Collections.enumeration(new HashSet()));

        Injector delegate = (Injector) Proxy.newProxyInstance(
                AbstractMVPApplicationTestModule.class.getClassLoader(),
                new Class[]{Injector.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    return method.invoke(MVPTestRunner.getInjector(), args);
                } catch (InvocationTargetException e) {
                    Throwable t = e.getCause();
                    if (t != null) {
                        throw t;
                    } else {
                        throw e;
                    }
                }
            }
        });

        when(servletContext.getAttribute(Injector.class.getName())).thenReturn(delegate);
        return servletContext;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void bindComponents() {
        install(createLoggerModule());
        install(createEventBusModule());
        install(createPresenterMapperModule());
        install(createComponentPreconfigurationModule());
    }

    protected LoggerModule createLoggerModule() {
        return new LoggerModule();
    }

    protected EventBusModule createEventBusModule() {
        return new EventBusModule(servletContext);
    }

    protected PresenterMapperModule createPresenterMapperModule() {
        return new PresenterMapperModule(servletContext);
    }

    protected VaadinComponentPreconfigurationModule createComponentPreconfigurationModule() {
        return new VaadinComponentPreconfigurationModule(servletContext);
    }

}
