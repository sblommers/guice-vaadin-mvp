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
import com.google.code.vaadin.application.MVPApplicationInitParameters;
import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.code.vaadin.localization.TextBundle;
import com.vaadin.server.UIProvider;

import javax.servlet.ServletContext;
import java.util.Collections;
import java.util.HashSet;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

/**
 * Base module for endpoint application test module.
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
        when(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION_MODULE)).thenReturn(getClass().getName());
        when(servletContext.getInitParameterNames()).thenReturn(Collections.enumeration(new HashSet()));
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
        bind(TextBundle.class).toInstance(new TextBundle() {
            @Override
            public String getText(String key, Object... params) {
                return key;
            }
        });
    }
}