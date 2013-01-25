/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.internal.servlet.MVPApplicationInitParameters;
import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.Observes;
import com.google.code.vaadin.mvp.View;
import com.google.code.vaadin.mvp.events.ViewInitializedEvent;
import com.google.code.vaadin.mvp.events.ViewOpenedEvent;
import com.google.inject.Binding;
import com.google.inject.Injector;
import com.google.inject.Scope;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PresenterInitializer - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class PresenterMapper {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Logger logger;
    protected Map<Class<? extends View>, Class<? extends AbstractPresenter>> viewPresenterMap;
    protected Injector injector;
    protected Class applicationClass;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    public void init(Logger logger, Injector injector, @Named(MVPApplicationInitParameters.P_APPLICATION) Class applicationClass) {
        this.logger = logger;
        this.injector = injector;
        this.applicationClass = applicationClass;

        //1. find all presenters
        Reflections reflections = new Reflections(createReflectionsConfiguration());
        Set<Class<? extends AbstractPresenter>> subTypesOf = reflections.getSubTypesOf(AbstractPresenter.class);

        //2. create context map: Presenter <-> View interface
        viewPresenterMap = new ConcurrentHashMap<Class<? extends View>, Class<? extends AbstractPresenter>>();
        for (Class<? extends AbstractPresenter> presenterClass : subTypesOf) {
            Class<View> viewClass = TypeUtil.getTypeParameterClass(presenterClass, View.class);
            viewPresenterMap.put(viewClass, presenterClass);
            // Presenter should be always SessionScoped
            Binding<? extends AbstractPresenter> presenterBinding = injector.getBinding(presenterClass);
            if (!isSessionScoped(presenterBinding)){
                logger.error(String.format("Presenter [%s] is not Session-scoped. Please add @SessionScoped annotation or bind presenter to this scope explicitly", presenterClass.getName()));
            }
        }

        //3. Add listener for all ViewInitialized event - see viewInitialized method
    }

    private boolean isSessionScoped(Binding<? extends AbstractPresenter> presenterBinding) {
        final boolean[] sessionScoped = {false};
        presenterBinding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Object>(){
            @Override
            public Object visitScope(Scope scope) {
                if (scope.equals(ServletScopes.SESSION)){
                    sessionScoped[0] = true;
                }
                return super.visitScope(scope);
            }
        });
        return sessionScoped[0];
    }

    /*===========================================[ CLASS METHODS ]================*/

    protected Configuration createReflectionsConfiguration() {
        return new ConfigurationBuilder()
                .addUrls(ClasspathHelper.forClass(applicationClass))
                .setScanners(new SubTypesScanner());
    }

    @Observes
    public void viewInitialized(ViewInitializedEvent event) {
        Class<? extends View> viewInterface = event.getViewInterface();
        Class<? extends AbstractPresenter> presenterClass = viewPresenterMap.get(viewInterface);
        //4. Instantiate appropriate Presenter for View interface from event. Appropriate earlier created View will be injected - it's because SessionScope.
        injector.getInstance(presenterClass);
    }

    @Observes
    public void viewOpened(ViewOpenedEvent event) {
        Class<? extends View> viewInterface = event.getViewInterface();
        Class<? extends AbstractPresenter> presenterClass = viewPresenterMap.get(viewInterface);
        //5. Call viewOpened if appropriate event received from view
        injector.getInstance(presenterClass).viewOpened();
    }
}
