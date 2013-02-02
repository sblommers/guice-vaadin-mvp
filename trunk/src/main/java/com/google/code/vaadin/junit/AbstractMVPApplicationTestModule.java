/*
 * Copyright (C) 2013 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
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