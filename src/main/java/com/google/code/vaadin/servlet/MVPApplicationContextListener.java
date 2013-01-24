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

package com.google.code.vaadin.servlet;

import com.google.code.vaadin.guice.AbstractMVPApplicationModule;
import com.google.code.vaadin.guice.MVPApplicationServletModule;
import com.google.code.vaadin.guice.VaadinComponentPreconfigurationModule;
import com.google.code.vaadin.guice.event.EventPublisherModule;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.netflix.governator.guice.LifecycleInjector;
import com.vaadin.Application;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * A Guice specific listener class is required to configure the Modules, i.e., the Servlet class, the Application
 * implementation, and all other Guice-managed entities.
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public class MVPApplicationContextListener extends GuiceServletContextListener {

    /*===========================================[ STATIC VARIABLES ]=============*/

    public static final String P_APPLICATION = "application";
    //todo
    public static final String P_APPLICATION_MODULE = "application-module";

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Class<? extends Application> applicationClass;
    private Class<? extends AbstractMVPApplicationModule> mvpApplicationModuleClass;
    private Injector injector;
    private ServletContext servletContext;

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        servletContext = servletContextEvent.getServletContext();
        try {
            applicationClass = (Class<? extends Application>) Class.forName(servletContext.getInitParameter(P_APPLICATION));
        } catch (Exception e) {
            throw new MVPApplicationException("ERROR: Unable to instantiate com.vaadin.Application class. " +
                    "Please check your webapp deployment descriptor.", e);
        }

        try {
            mvpApplicationModuleClass = (Class<? extends AbstractMVPApplicationModule>) Class.forName(servletContext.getInitParameter(P_APPLICATION_MODULE));
        } catch (Exception e) {
            throw new MVPApplicationException("ERROR: Unable to instantiate com.vaadin.Application class. " +
                    "Please check your webapp deployment descriptor.", e);
        }

        super.contextInitialized(servletContextEvent);
    }

    /*===========================================[ GETTER/SETTER ]================*/

    @Override
    protected Injector getInjector() {
        if (injector == null) {
            injector = createInjector();
        }

        return injector;
    }

    protected Injector createInjector() {
        try {
            List<Module> modules = new ArrayList<Module>();
            // default module is always first
            modules.add(createDefaultModule());
            modules.add(createApplicationModule());
            // support for @Preconfigured last because it depends on TextBundle bindings
            modules.add(new VaadinComponentPreconfigurationModule());
            return LifecycleInjector.builder().withModules(modules).createInjector();
        } catch (Exception e) {
            throw new MVPApplicationException("Unable to instantiate Injector", e);
        }
    }

    protected Module createDefaultModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                install(new MVPApplicationServletModule(applicationClass));
                install(new EventPublisherModule());
            }
        };
    }

    protected AbstractMVPApplicationModule createApplicationModule() throws Exception {
        Constructor<? extends AbstractMVPApplicationModule> constructor = mvpApplicationModuleClass.getConstructor(ServletContext.class);
        return constructor.newInstance(servletContext);
    }
}