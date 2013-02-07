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

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.junit.util.ServletTestUtils;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceFilter;
import com.google.inject.servlet.GuiceFilterResetter;
import com.vaadin.server.VaadinRequest;
import com.vaadin.ui.UI;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.nnsoft.guice.junice.JUniceRunner;
import org.nnsoft.guice.junice.reflection.HandleException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * MVPTestRunner - TODO: description
 *
 * @author Alexey Krylov
 * @since 28.01.13
 */
public class MVPTestRunner extends JUniceRunner {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(MVPTestRunner.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Injector injector;
    private ServletContext servletContext;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public MVPTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        try {
            // ideas from guice-servlet JUnit-tests
            GuiceFilterResetter.reset();
            GuiceFilter filter = new GuiceFilter();
            filter.init(getFilterConfig());

            FilterChain filterChain = new FilterChain() {
                @Override
                public void doFilter(ServletRequest request,
                                     ServletResponse response) {
                    MVPTestRunner.super.runChild(method, notifier);
                }
            };

            filter.doFilter(ServletTestUtils.newFakeHttpServletRequest(), ServletTestUtils.newFakeHttpServletResponse(), filterChain);
        } catch (Exception e) {
            logger.error("Error", e);
            throw new MVPApplicationException(e);
        }
    }

    @Override
    protected List<Module> inizializeInjector(Class<?> clazz) throws HandleException, InstantiationException, IllegalAccessException {
        List<Module> modules = super.inizializeInjector(clazz);
        servletContext = createServletContext();
        modules.add(new BaseMVPApplicationTestModule(servletContext));
        return modules;
    }

    protected ServletContext createServletContext() {
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION_UI_CLASS)).thenReturn(getApplicationUIClass().getName());
        when(servletContext.getInitParameterNames()).thenReturn(Collections.enumeration(new HashSet()));

        Injector delegate = (Injector) Proxy.newProxyInstance(
                BaseMVPApplicationTestModule.class.getClassLoader(),
                new Class[]{Injector.class}, new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
                try {
                    return method.invoke(injector, args);
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

    protected Class<? extends UI> getApplicationUIClass() {
        return MVPTestApplicationUI.class;
    }

    @Override
    protected Injector createInjector(List<Module> modules) {
        Injector injector = super.createInjector(modules);
        this.injector = injector;
        return injector;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    private FilterConfig getFilterConfig() {
        FilterConfig filterConfig = mock(FilterConfig.class);
        when(filterConfig.getServletContext()).thenReturn(servletContext);
        return filterConfig;
    }

    protected Injector getInjector() {
        return injector;
    }

	/*===========================================[ INNER CLASSES ]================*/

    private static class MVPTestApplicationUI extends ScopedUI{

        private static final long serialVersionUID = 5297142885176304733L;

        @Override
        protected void init(VaadinRequest request) {

        }
    }
}
