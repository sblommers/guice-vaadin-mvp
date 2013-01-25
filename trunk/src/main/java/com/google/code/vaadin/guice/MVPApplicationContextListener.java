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

package com.google.code.vaadin.guice;

import com.google.code.vaadin.internal.components.VaadinComponentPreconfigurationModule;
import com.google.code.vaadin.internal.event.EventPublisherModule;
import com.google.code.vaadin.internal.logging.LoggerModule;
import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.List;

/**
 * A Guice specific listener class is required to configure the Modules, i.e., the Servlet class, the Application
 * implementation, and all other Guice-managed entities.
 *
 * @author Alexey Krylov
 * @since 23.01.13
 */
public class MVPApplicationContextListener extends GuiceServletContextListener {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(MVPApplicationContextListener.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Class<? extends AbstractMVPApplicationModule> mvpApplicationModuleClass;
    private Injector injector;
    private ServletContext servletContext;

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        logger.info("Initializing Guice-Vaadin-MVP context...");

        servletContext = servletContextEvent.getServletContext();

        try {
            mvpApplicationModuleClass = (Class<? extends AbstractMVPApplicationModule>) Class.forName(servletContext.getInitParameter(MVPApplicationInitParameters.P_APPLICATION_MODULE));
        } catch (Exception e) {
            throw new MVPApplicationException(String.format("ERROR: Unable to instantiate class of [%s]. " +
                    "Please check your webapp deployment descriptor.", AbstractMVPApplicationModule.class.getName()), e);
        }

        super.contextInitialized(servletContextEvent);
        //todo presenter никогда не инжектится и поэтому не может зарегистрироваться в подписках
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("Destroying Guice-Vaadin-MVP context...");
        super.contextDestroyed(servletContextEvent);
        logger.info("Context successfully destroyed");
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
            logger.info("Creating Injector...");
            List<Module> modules = new ArrayList<Module>();
            modules.add(createLoggerModule());
            modules.add(createEventPublisherModule());
            modules.add(createApplicationModule());
            // support for @Preconfigured last because it depends on TextBundle bindings in ApplicationModule
            modules.add(new VaadinComponentPreconfigurationModule());

            Injector injector = Guice.createInjector(modules);
            logger.info("Injector successfully created");
            return injector;
        } catch (Exception e) {
            throw new MVPApplicationException("Unable to instantiate Injector", e);
        }
    }

    protected LoggerModule createLoggerModule() {
        return new LoggerModule();
    }

    protected EventPublisherModule createEventPublisherModule() {
        return new EventPublisherModule();
    }

    protected AbstractMVPApplicationModule createApplicationModule() throws Exception {
        Constructor<? extends AbstractMVPApplicationModule> constructor = mvpApplicationModuleClass.getConstructor(ServletContext.class);
        return constructor.newInstance(servletContext);
    }
}