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

package com.google.inject.servlet;

import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.servlet.ServletTestUtils;
import com.google.inject.Injector;
import com.google.inject.Module;
import org.junit.runner.notification.RunNotifier;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;
import org.nnsoft.guice.junice.JUniceRunner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * MVPTestRunner - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 28.01.13
 */
public class MVPTestRunner extends JUniceRunner {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(MVPTestRunner.class);

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private static Injector injector;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public MVPTestRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

	/*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void runChild(final FrameworkMethod method, final RunNotifier notifier) {
        try {
            GuiceFilter.reset();
            GuiceFilter filter = new GuiceFilter();
            filter.init(getFilterConfig());

            final CountDownLatch countDownLatch = new CountDownLatch(1);
            FilterChain filterChain = new FilterChain() {
                @Override
                public void doFilter(ServletRequest servletRequest,
                                     ServletResponse servletResponse) {
                    MVPTestRunner.super.runChild(method, notifier);
                    countDownLatch.countDown();
                }
            };

            //todo doFilter has not been invoked
            filter.doFilter(ServletTestUtils.newFakeHttpServletRequest(), ServletTestUtils.newFakeHttpServletResponse(), filterChain);
            countDownLatch.await(1, TimeUnit.SECONDS);
        } catch (Exception e) {
            logger.error("Error", e);
            throw new RuntimeException(e);
        }
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
        ServletContext servletContext = mock(ServletContext.class);
        when(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION)).thenReturn(getClass().getName());
        when(servletContext.getInitParameterNames()).thenReturn(Collections.enumeration(new HashSet()));
        when(servletContext.getAttribute(Injector.class.getName())).thenReturn(injector);
        when(filterConfig.getServletContext()).thenReturn(servletContext);
        return filterConfig;
    }

    public static Injector getInjector() {
        return injector;
    }
}
