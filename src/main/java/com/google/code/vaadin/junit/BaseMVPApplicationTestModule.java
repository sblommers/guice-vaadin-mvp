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
import com.google.code.vaadin.internal.localization.ResourceBundleInjectionModule;
import com.google.code.vaadin.internal.logging.LoggerModule;
import com.google.code.vaadin.internal.mapping.PresenterMapperModule;

import javax.servlet.ServletContext;

/**
 * TestMVPApplicationModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class BaseMVPApplicationTestModule extends AbstractMVPApplicationModule {

    /*===========================================[ CONSTRUCTORS ]=================*/

    protected BaseMVPApplicationTestModule(ServletContext servletContext) {
        super(servletContext);
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void installModules() {
        install(createLoggerModule());
        install(createEventBusModule());
        install(createResourceBundleInjectionModule());
        install(createPresenterMapperModule());
        install(createComponentPreconfigurationModule());
    }

    @Override
    protected void bindComponents() {
    }

    @Override
    protected void bindTextBundle() {
    }

    protected LoggerModule createLoggerModule() {
        return new LoggerModule();
    }

    protected EventBusModule createEventBusModule() {
        return new EventBusModule(servletContext);
    }

    protected ResourceBundleInjectionModule createResourceBundleInjectionModule() {
        return new ResourceBundleInjectionModule();
    }

    protected PresenterMapperModule createPresenterMapperModule() {
        return new PresenterMapperModule(servletContext);
    }

    protected VaadinComponentPreconfigurationModule createComponentPreconfigurationModule() {
        return new VaadinComponentPreconfigurationModule(servletContext);
    }
}