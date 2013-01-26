/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.ModelEventPublisher;
import com.google.code.vaadin.mvp.ViewEventPublisher;
import com.google.inject.AbstractModule;
import com.google.inject.BindingAnnotation;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.servlet.ServletContext;
import java.lang.annotation.*;

/**
 * EventPublisherModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class EventBusModule extends AbstractModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private ServletContext servletContext;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public EventBusModule(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        // Registers all injectees as EventBus subscribers because we can't definitely say who is listening
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
/*
                for (Field field : typeLiteral.getRawType().getDeclaredFields()) {
                    if (Component.class.isAssignableFrom(field.getType())
                            && field.isAnnotationPresent(Preconfigured.class)) {
                        typeEncounter.register(new EventPublisherInjector(servletContext));

                        typeEncounter.register(new VaadinComponentsInjector<T>(field, preconfigured, servletContext));
                    }
                }
                typeEncounter.register(new VaadinComponentsInjector<T>(field, preconfigured, servletContext));
*/
                typeEncounter.register(new EventPublisherInjector<I>(servletContext));
               /* typeEncounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        Injector injector = InjectorProvider.getInjector(servletContext);
                        IMessageBus viewEventBus = injector.getInstance(Key.get(IMessageBus.class, ViewEventBus.class));
                        IMessageBus modelEventBus = injector.getInstance(Key.get(IMessageBus.class, ModelEventBus.class));
                        //TODO do not subscribe classes without Observes methods
                        //TODO EP should be removed by GC, but needs to be checked. It will contain links on different scoped components, but only session scoped will have links to this EP.
                        viewEventBus.subscribe(injectee);
                        modelEventBus.subscribe(injectee);
                    }
                });*/
            }
        });

        //todo bind of IMEssageBus
        bind(EventBus.class).annotatedWith(ModelEventBus.class).toProvider(ModelEventBusProvider.class).in(Scopes.SINGLETON);
        bind(ModelEventPublisher.class).toProvider(ModelEventPublisherProvider.class).in(Scopes.SINGLETON);

        // Global Event Bus
        bind(EventBus.class).annotatedWith(GlobalViewEventBus.class).toProvider(GlobalViewEventBusProvider.class).in(Scopes.SINGLETON);

        //todo bind global event bus for views
        bind(EventBus.class).annotatedWith(ViewEventBus.class).toProvider(ViewEventBusProvider.class).in(ServletScopes.SESSION);
        bind(ViewEventPublisher.class).toProvider(ViewEventPublisherProvider.class).in(ServletScopes.SESSION);
    }

    @BindingAnnotation
    @Documented
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ViewEventBus {
    }

    @BindingAnnotation
    @Documented
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface GlobalViewEventBus {
    }

    @BindingAnnotation
    @Documented
    @Target({ElementType.FIELD, ElementType.PARAMETER})
    @Retention(RetentionPolicy.RUNTIME)
    public @interface ModelEventBus {
    }
}
