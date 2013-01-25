/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.mapping;

import com.google.code.vaadin.guice.AbstractMVPApplicationModule;
import com.google.code.vaadin.internal.util.ApplicationClassProvider;
import com.google.code.vaadin.mvp.AbstractPresenter;
import com.google.code.vaadin.mvp.AbstractView;
import com.google.code.vaadin.mvp.View;
import com.google.inject.*;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.DefaultBindingScopingVisitor;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.SubTypesScanner;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * PresenterMappingModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 25.01.13
 */
public class PresenterMapperModule extends AbstractModule {

    private static final Logger logger = LoggerFactory.getLogger(PresenterMapperModule.class);

    protected Map<Class<? extends View>, Class<? extends AbstractPresenter>> viewPresenterMap;
    protected Injector injector;
    protected Class<? extends AbstractMVPApplicationModule> applicationClass;
    protected ServletContext servletContext;

    public PresenterMapperModule(ServletContext servletContext) {
        this.servletContext = servletContext;
        applicationClass = ApplicationClassProvider.getApplicationClass(servletContext);
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        //1. find all presenters
        Reflections reflections = new Reflections(createReflectionsConfiguration());
        Set<Class<? extends AbstractPresenter>> subTypesOf = reflections.getSubTypesOf(AbstractPresenter.class);

        //2. create context map: Presenter <-> View interface
        viewPresenterMap = new ConcurrentHashMap<Class<? extends View>, Class<? extends AbstractPresenter>>();
        for (Class<? extends AbstractPresenter> presenterClass : subTypesOf) {
            Class<View> viewClass = TypeUtil.getTypeParameterClass(presenterClass, View.class);
            viewPresenterMap.put(viewClass, presenterClass);
            // Presenter should be always SessionScoped
            /* Binding<? extends AbstractPresenter> presenterBinding = injector.getBinding(presenterClass);
            if (!isSessionScoped(presenterBinding)) {
                logger.error(String.format("Presenter [%s] is not Session-scoped. Please add @SessionScoped annotation or bind presenter to this scope explicitly!", presenterClass.getName()));
            }*/
        }


        //3. Add listener for all ViewInitialized event - see viewInitialized method
        //bind(PresenterMapper.class).asEagerSingleton();

        bindListener(Matchers.any(), new ViewTypeListener());
    }

    /*===========================================[ CONSTRUCTORS ]=================*/

    private boolean isSessionScoped(Binding<? extends AbstractPresenter> presenterBinding) {
        final boolean[] sessionScoped = {false};
        presenterBinding.acceptScopingVisitor(new DefaultBindingScopingVisitor<Object>() {
            @Override
            public Object visitScope(Scope scope) {
                if (scope.equals(ServletScopes.SESSION)) {
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

    /* @Observes
    public void viewInitialized(ViewInitializedEvent event) {
        Class<? extends View> viewInterface = event.getViewInterface();
        Class<? extends AbstractPresenter> presenterClass = viewPresenterMap.get(viewInterface);
        //4. Instantiate appropriate Presenter for View interface from event. Appropriate earlier created View will be injected - it's because SessionScope.
        injector.getInstance(presenterClass);
    }*/



    //TODO SessionScoped bean с маппингами
     //TODO проблемы с открытием параллельной сессии
    private class ViewTypeListener implements TypeListener {
        @Override
        public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
            typeEncounter.register(new InjectionListener<I>() {
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
                        Injector injector = (Injector) servletContext.getAttribute(Injector.class.getName());

                        AbstractPresenter presenter = injector.getInstance(presenterClass);
                        presenter.setView(view);
                        injector.getInstance(MappingContext.class).addMapping(view, presenter);
                    }
                }
            });
        }
    }
}
