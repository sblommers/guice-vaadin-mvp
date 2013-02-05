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

import com.google.code.vaadin.internal.util.ApplicationClassProvider;
import com.google.code.vaadin.internal.util.InjectorProvider;
import com.google.code.vaadin.internal.util.TypeUtil;
import com.google.code.vaadin.mvp.*;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PresenterMappingModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 25.01.13
 */
public class PresenterMapperModule extends AbstractModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Map<Class<? extends View>, Class<? extends AbstractPresenter>> viewPresenterMap;
    protected Injector injector;
    protected Class<? extends AbstractUI> applicationClass;
    protected ServletContext servletContext;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public PresenterMapperModule(ServletContext servletContext) {
        this.servletContext = servletContext;
        applicationClass = ApplicationClassProvider.getApplicationClass(servletContext);
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(ViewPresenterMappingRegistry.class).to(DefaultViewPresenterMappingRegistry.class).in(ServletScopes.SESSION);

        //1. find all presenters
        Reflections reflections = new Reflections(createReflectionsConfiguration());
        Set<Class<? extends AbstractPresenter>> subTypesOf = reflections.getSubTypesOf(AbstractPresenter.class);

        //2. create context map: View interface -> Presenter class
        viewPresenterMap = new ConcurrentHashMap<Class<? extends View>, Class<? extends AbstractPresenter>>();
        for (Class<? extends AbstractPresenter> presenterClass : subTypesOf) {
            Class<View> viewClass = TypeUtil.getTypeParameterClass(presenterClass, View.class);
            viewPresenterMap.put(viewClass, presenterClass);
        }

        //3. Add listener for all ViewInitialized event - see viewInitialized method
        //bind(PresenterMapper.class).asEagerSingleton();
        bindListener(Matchers.any(), new ViewTypeListener());
    }

    /*===========================================[ CLASS METHODS ]================*/

    protected Configuration createReflectionsConfiguration() {
        return new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClass(applicationClass))
                .setScanners(new SubTypesScanner());
    }

    /*===========================================[ INNER CLASSES ]================*/

    private class ViewTypeListener implements TypeListener {
        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            encounter.register(new InjectionListener<I>() {
                @Override
                public void afterInjection(I injectee) {
                    if (injectee instanceof View) {
                        View view = (View) injectee;
                        Class<? extends View> viewInterface = view.getClass();

                        if (view instanceof AbstractView) {
                            viewInterface = ((AbstractView) view).getViewInterface();
                        }

                        //4. Instantiate appropriate Presenter for View interface from event. Appropriate earlier created View will be injected - it's because SessionScope.
                        Class<? extends AbstractPresenter> presenterClass = viewPresenterMap.get(viewInterface);

                        Injector injector = InjectorProvider.getInjector(servletContext);
                        AbstractPresenter presenter = injector.getInstance(presenterClass);
                        presenter.setView(view);
                        DefaultViewPresenterMappingRegistry mappingRegistry = injector.getInstance(DefaultViewPresenterMappingRegistry.class);
                        mappingRegistry.registerMapping(view, presenter);
                        // Instantiate support for Presenter.viewOpened
                        injector.getInstance(ViewOpenedEventRedirector.class);
                    }
                }
            });
        }
    }
}
