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

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.application.AbstractMVPApplicationModule;
import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.components.mapping.ViewPresenterMappingRegistry;
import com.google.code.vaadin.components.mapping.ViewProvider;
import com.google.code.vaadin.internal.util.ApplicationUIClassProvider;
import com.google.code.vaadin.internal.util.TypeUtil;
import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.View;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeListener;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.*;

/**
 * View -> Presenter mapping support components.
 * It supports two ways of configuration - classpath scan and direct specification of Presenter classes.
 *
 * @author Alexey Krylov
 * @see AbstractMVPApplicationModule#getPresenterClasses()
 * @since 25.01.13
 */
public class PresenterMapperModule extends AbstractModule {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(PresenterMapperModule.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Class<? extends ScopedUI> applicationClass;
    protected ServletContext servletContext;
    protected Collection<Class<? extends AbstractPresenter<? extends View>>> presenterClasses;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public PresenterMapperModule(ServletContext servletContext, Collection<Class<? extends AbstractPresenter<? extends View>>> presenterClasses) {
        this.servletContext = servletContext;
        applicationClass = ApplicationUIClassProvider.getApplicationClass(servletContext);
        this.presenterClasses = new ArrayList<>(presenterClasses);
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(ViewPresenterMappingRegistry.class).to(AccessibleViewPresenterMappingRegistry.class).in(UIScope.getCurrent());
        bind(ViewProvider.class).to(AccessibleViewProvider.class).in(UIScope.getCurrent());
        //1. find all presenters
        Collection<Class<? extends AbstractPresenter<? extends View>>> presenterClasses = new ArrayList<>(this.presenterClasses);
        if (presenterClasses.isEmpty()) {
            presenterClasses.addAll(findPresenterClasses());
        }

        //2. create context map: View interface -> Presenter class
        Map<Class<? extends View>, Class<? extends AbstractPresenter>> viewPresenterMap = new HashMap<>();
        for (Class<? extends AbstractPresenter> presenterClass : presenterClasses) {
            Class<View> viewClass = TypeUtil.getTypeParameterClass(presenterClass, View.class);
            viewPresenterMap.put(viewClass, presenterClass);
            logger.debug(String.format("[%s] mapped", presenterClass.getName()));
        }

        //3. Add listener for all ViewInitialized event - see viewInitialized method
        TypeListener viewTypeListener = new ViewTypeListener(viewPresenterMap);
        requestInjection(viewTypeListener);
        bindListener(Matchers.any(), viewTypeListener);
    }

    protected Collection<Class<? extends AbstractPresenter<? extends View>>> findPresenterClasses() {
        Collection<Class<? extends AbstractPresenter<? extends View>>> result = new ArrayList<>();
        Reflections reflections = new Reflections(createReflectionsConfiguration());
        result.addAll((Set) reflections.getSubTypesOf(AbstractPresenter.class));
        return result;
    }

    protected Configuration createReflectionsConfiguration() {
        return new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClass(applicationClass))
                .setScanners(new SubTypesScanner());
    }
}