/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.internal.util.InjectorProvider;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Scopes;
import net.engio.mbassy.IMessageBus;
import net.engio.mbassy.common.ReflectionUtils;

import javax.servlet.ServletContext;

/**
 * EventPublisherInjector - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class EventPublisherInjector<T> implements MembersInjector<T> {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private final ServletContext servletContext;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public EventPublisherInjector(ServletContext servletContext) {
        this.servletContext = servletContext;
    }

    /*===========================================[ INTERFACE METHODS ]============*/
    @Override
    public void injectMembers(T instance) {
        Injector injector = InjectorProvider.getInjector(servletContext);
        Class<?> instanceClass = instance.getClass();

        // Do not subscribe classes without @Observes/@Listener methods
        if (injector != null && !ReflectionUtils.getMethods(CompositeMetadataReader.AllMessageHandlers, instanceClass).isEmpty()) {
            // Do not allowed to register Singleton as Session-scoped EventBus subscriber
            if (!Scopes.isSingleton(injector.getBinding(instanceClass))) {
                IMessageBus viewEventBus = injector.getInstance(Key.get(IMessageBus.class, EventPublisherModule.ViewEventBus.class));
                viewEventBus.subscribe(instance);
            }
            IMessageBus modelEventBus = injector.getInstance(Key.get(IMessageBus.class, EventPublisherModule.ModelEventBus.class));
            //TODO EP should be removed by GC, but needs to be checked. It will contain links on different scoped components, but only session scoped will have links to this EP.
            //TODO: model eventbus will hold references to all...
            modelEventBus.subscribe(instance);
        }
    }
}
