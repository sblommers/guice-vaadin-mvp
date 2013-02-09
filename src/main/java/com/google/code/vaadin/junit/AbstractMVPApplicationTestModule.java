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

import com.google.code.vaadin.application.AbstractMVPApplicationModule;
import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.inject.Injector;
import com.vaadin.server.UIProvider;

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

    protected AbstractMVPApplicationTestModule() {
        super(mock(ServletContext.class));
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void configureServlets() {
        activateServletContext();
        super.configureServlets();
    }

    protected ServletContext activateServletContext() {
        when(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION_UI_CLASS)).thenReturn(getTestUIClass().getName());
        when(servletContext.getInitParameterNames()).thenReturn(Collections.enumeration(new HashSet()));

        //todo provider
        Injector delegate = (Injector) Proxy.newProxyInstance(
                AbstractMVPApplicationTestModule.class.getClassLoader(),
                new Class[]{Injector.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    return method.invoke(null, args);
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

    @Override
    protected void bindUIProvider() {
        bind(UIProvider.class).to(ScopedUIProvider.class);
        bind(ScopedUIProvider.class).to(TestScopedUIProvider.class);
    }

    protected abstract Class<? extends ScopedUI> getTestUIClass();

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void installModules() {
    }

    @Override
    protected void bindComponents() {
    }

    @Override
    protected void bindTextBundle() {
        //todo bind default impl
    }
}
