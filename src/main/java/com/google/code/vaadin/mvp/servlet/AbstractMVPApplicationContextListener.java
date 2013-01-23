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

package com.google.code.vaadin.mvp.servlet;

import com.google.code.vaadin.mvp.MVPApplicationException;
import com.google.code.vaadin.mvp.guice.event.EventPublisherModule;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.servlet.GuiceServletContextListener;
import com.netflix.governator.guice.LifecycleInjector;
import com.vaadin.Application;

import javax.servlet.ServletContextEvent;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * AbstractMVPApplicationConfig - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 23.01.13
 */
public abstract class AbstractMVPApplicationContextListener extends GuiceServletContextListener {

	/*===========================================[ STATIC VARIABLES ]=============*/

    public static final String P_APPLICATION = "application";

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Class<? extends Application> applicationClass;
    private Injector injector;

	/*===========================================[ CLASS METHODS ]================*/

    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        try {
            applicationClass = (Class<? extends Application>) Class.forName(servletContextEvent.getServletContext().getInitParameter(P_APPLICATION));
        } catch (Exception e) {
            throw new MVPApplicationException("ERROR: Unable to instantiate com.vaadin.Application class. " +
                    "Please check your webapp deployment descriptor.", e);
        }

        super.contextInitialized(servletContextEvent);
/*
//todo check correctness of LifecycleManager start/stop in the Application
        try {
            injector.getInstance(LifecycleManager.class).start();
        } catch (Exception e) {
            throw new MVPApplicationException("ERROR: Unable to start LifecycleManager", e);
        }*/
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
        List<Module> modules = new ArrayList<Module>(getModules());
        modules.add(0, createDefaultModule());
        return LifecycleInjector.builder().withModules(modules).createInjector();
    }

    protected abstract Collection<? extends Module> getModules();

    protected Module createDefaultModule() {
        return new AbstractModule() {
            @Override
            protected void configure() {
                install(new MVPApplicationServletModule(applicationClass));
                install(new EventPublisherModule());
            }
        };
    }
}