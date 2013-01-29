/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.internal.util.InjectorProvider;
import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.EventBuses;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Scopes;
import net.engio.mbassy.common.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletContext;

/**
 * EventPublisherInjector - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
class EventPublisherInjector<T> implements MembersInjector<T> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(EventPublisherInjector.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private final ServletContext servletContext;

    /*===========================================[ CONSTRUCTORS ]=================*/

    EventPublisherInjector(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        Injector injector = InjectorProvider.getInjector(servletContext);
        Class<?> instanceClass = instance.getClass();

        // Subscribe only if @Observes/@Listener methods present
        if (injector != null && !ReflectionUtils.getMethods(CompositeMetadataReader.AllMessageHandlers, instanceClass).isEmpty()) {
            /**
             * Do not allowed to register Singleton-scoped instances as Session-scoped ViewEventBus subscriber.
             * This case only possible for not eager singletons that is injected by Session-scoped components.
             */
            if (!Scopes.isSingleton(injector.getBinding(instanceClass))) {
                EventBus viewEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.ViewEventBus.class));
                EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.ModelEventBus.class));
                injector.getInstance(DefaultMVPApplicationContext.class).registerSessionScopedSubscriber(instance);
                viewEventBus.subscribe(instance);
                modelEventBus.subscribe(instance);
                logger.info(String.format("[%s] subscribed to ViewEventBus [#%d] and ModelEventBus [#%d]", instance.toString(), viewEventBus.hashCode(), modelEventBus.hashCode()));
            }

            EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.GlobalModelEventBus.class));
            modelEventBus.subscribe(instance);
            logger.info(String.format("[%s] subscribed to GlobalModelEventBus [#%d]", instance, modelEventBus.hashCode()));
        }
    }
}