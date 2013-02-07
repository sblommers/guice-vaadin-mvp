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

package com.google.code.vaadin.application;

import com.google.code.vaadin.application.uiscope.UIScopeModule;
import com.google.code.vaadin.internal.components.VaadinComponentPreconfigurationModule;
import com.google.code.vaadin.internal.event.EventBusModule;
import com.google.code.vaadin.internal.event.EventBusSubscribersRegistry;
import com.google.code.vaadin.internal.localization.ResourceBundleInjectionModule;
import com.google.code.vaadin.internal.logging.LoggerModule;
import com.google.code.vaadin.internal.mapping.PresenterMapperModule;
import com.google.code.vaadin.internal.util.ApplicationModuleClassProvider;
import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.EventBuses;
import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.*;
import javax.servlet.http.HttpSession;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;
import java.lang.reflect.Constructor;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;

/**
 * A Guice specific listener class is required to configure the Modules, i.e., the Servlet class, the Application
 * implementation, and all other Guice-managed entities.
 *
 * @author Alexey Krylov
 * @since 23.01.13
 */
public class MVPApplicationContextListener extends GuiceServletContextListener implements HttpSessionListener,
        ServletRequestListener {

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

        mvpApplicationModuleClass = ApplicationModuleClassProvider.getApplicationModuleClass(servletContext);
        super.contextInitialized(servletContextEvent);
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        logger.info("Destroying Guice-Vaadin-MVP context...");
        super.contextDestroyed(servletContextEvent);
        logger.info("Context successfully destroyed");
    }

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        logger.debug(String.format("Created session: [%s]", se.getSession().getId()));
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        HttpSession session = se.getSession();
        String sessionID = session.getId();
        logger.debug(String.format("Destroying data of session: [%s]", sessionID));
        unsubscribeSessionScopedSubscribers(sessionID);
        if (logger.isDebugEnabled()) {
            Enumeration attributeNames = session.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement().toString();
                logger.debug(String.format("SESSION attribute: [%s]", session.getAttribute(name)));
            }
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Request initialized: [%s]", sre.getServletRequest()));
        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        ServletRequest request = sre.getServletRequest();
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Destroying data of request: [%s]", request));
            Enumeration attributeNames = request.getAttributeNames();
            while (attributeNames.hasMoreElements()) {
                String name = attributeNames.nextElement().toString();
                logger.debug(String.format("REQUEST attribute: [%s]", request.getAttribute(name)));
            }
        }
    }

    protected void unsubscribeSessionScopedSubscribers(String sessionID) {
        Iterable sessionScopedSubscribers = injector.getInstance(EventBusSubscribersRegistry.class).getAndRemoveSessionScopedSubscribers(sessionID);
        EventBus globalModelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.GlobalModelEventBus.class));
        if (sessionScopedSubscribers != null) {
            for (Object subscriber : sessionScopedSubscribers) {
                globalModelEventBus.unsubscribe(subscriber);
            }
        }
    }

    /*===========================================[ GETTER/SETTER ]================*/

    @Override
    protected Injector getInjector() {
        try {
            logger.info("Creating Injector...");
            Collection<Module> modules = new ArrayList<Module>();
            modules.add(createLoggerModule());
            modules.add(createEventBusModule());
            modules.add(createResourceBundleInjectionModule());
            modules.add(createUIScopeModule());
            modules.add(createApplicationModule());
            modules.add(createPresenterMapperModule());
            // support for @Preconfigured last because it depends on TextBundle bindings in ApplicationModule
            modules.add(createComponentPreconfigurationModule());

            Injector injector = Guice.createInjector(modules);
            this.injector = injector;
            logger.info("Injector successfully created");
            return injector;
        } catch (Exception e) {
            throw new MVPApplicationException("Unable to instantiate Injector", e);
        }
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

    protected UIScopeModule createUIScopeModule() {
        return new UIScopeModule();
    }

    protected PresenterMapperModule createPresenterMapperModule() {
        return new PresenterMapperModule(servletContext);
    }

    protected AbstractMVPApplicationModule createApplicationModule() throws Exception {
        Constructor<? extends AbstractMVPApplicationModule> constructor = mvpApplicationModuleClass.getConstructor(ServletContext.class);
        return constructor.newInstance(servletContext);
    }

    protected VaadinComponentPreconfigurationModule createComponentPreconfigurationModule() {
        return new VaadinComponentPreconfigurationModule(servletContext);
    }
}