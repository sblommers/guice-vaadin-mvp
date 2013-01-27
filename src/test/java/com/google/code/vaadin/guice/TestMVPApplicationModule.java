/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.guice;

import com.google.code.vaadin.TextBundle;
import com.google.code.vaadin.internal.components.VaadinComponentPreconfigurationModule;
import com.google.code.vaadin.internal.event.EventBusModule;
import com.google.code.vaadin.internal.logging.LoggerModule;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.inject.Injector;

import javax.servlet.ServletContext;
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
public class TestMVPApplicationModule extends AbstractMVPApplicationModule {

    /*===========================================[ CONSTRUCTORS ]=================*/

    public TestMVPApplicationModule() {
        super(createMockedServletContext());
    }

    protected static ServletContext createMockedServletContext() {
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION)).thenReturn(TestMVPApplicationModule.class.getName());
        when(servletContext.getInitParameterNames()).thenReturn(Collections.enumeration(new HashSet()));

        Injector injectorMock = mock(Injector.class);
        when(injectorMock.getInstance(TextBundle.class)).thenReturn(new TextBundle() {
            @Override
            public String getText(String key, Object... params) {
                return String.format(key, params);
            }
        });

        when(servletContext.getAttribute(Injector.class.getName())).thenReturn(injectorMock);
        return servletContext;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void bindComponents() {
        install(new LoggerModule());
        install(new EventBusModule(servletContext));
        install(new VaadinComponentPreconfigurationModule(servletContext));
    }

    @Override
    protected void bindTextBundle() {
        bind(TextBundle.class).toInstance(new TextBundle() {
            @Override
            public String getText(String key, Object... params) {
                return String.format(key, params);
            }
        });
    }
}
