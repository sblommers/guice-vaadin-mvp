/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.internal.event.eventbus.GlobalModelEventBusProvider;
import com.google.code.vaadin.internal.event.eventbus.GlobalViewEventBusProvider;
import com.google.code.vaadin.internal.event.eventbus.ModelEventBusProvider;
import com.google.code.vaadin.internal.event.eventbus.ViewEventBusProvider;
import com.google.code.vaadin.internal.event.messagebus.GlobalModelMessageBusProvider;
import com.google.code.vaadin.internal.event.messagebus.GlobalViewMessageBusProvider;
import com.google.code.vaadin.internal.event.messagebus.ModelMessageBusProvider;
import com.google.code.vaadin.internal.event.messagebus.ViewMessageBusProvider;
import com.google.code.vaadin.internal.event.publisher.GlobalModelEventPublisherProvider;
import com.google.code.vaadin.internal.event.publisher.ModelEventPublisherProvider;
import com.google.code.vaadin.internal.event.publisher.ViewEventPublisherProvider;
import com.google.code.vaadin.mvp.*;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.servlet.ServletScopes;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.engio.mbassy.IMessageBus;

import javax.servlet.ServletContext;

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
            public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
                encounter.register(new EventPublisherInjector<I>(servletContext));
            }
        });

        // MessageBus
        bindMessageBus();
        // EventBus
        bindEventBus();
        // EventPublishers
        bindEventPublishers();
    }

    private void bindMessageBus() {
        bind(IMessageBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelMessageBusProvider.class).in(ServletScopes.SESSION);
        bind(IMessageBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewMessageBusProvider.class).in(ServletScopes.SESSION);
        bind(IMessageBus.class).annotatedWith(EventBuses.GlobalModelEventBus.class).toProvider(GlobalModelMessageBusProvider.class).in(Scopes.SINGLETON);
        bind(IMessageBus.class).annotatedWith(EventBuses.GlobalViewEventBus.class).toProvider(GlobalViewMessageBusProvider.class).in(Scopes.SINGLETON);
    }

    private void bindEventBus() {
        bind(EventBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelEventBusProvider.class).in(ServletScopes.SESSION);
        bind(EventBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewEventBusProvider.class).in(ServletScopes.SESSION);
        bind(EventBus.class).annotatedWith(EventBuses.GlobalModelEventBus.class).toProvider(GlobalModelEventBusProvider.class).in(Scopes.SINGLETON);
        bind(EventBus.class).annotatedWith(EventBuses.GlobalViewEventBus.class).toProvider(GlobalViewEventBusProvider.class).in(Scopes.SINGLETON);
    }

    private void bindEventPublishers() {
        bind(GlobalModelEventPublisher.class).toProvider(GlobalModelEventPublisherProvider.class).in(Scopes.SINGLETON);
        bind(ModelEventPublisher.class).toProvider(ModelEventPublisherProvider.class).in(ServletScopes.SESSION);
        bind(ViewEventPublisher.class).toProvider(ViewEventPublisherProvider.class).in(ServletScopes.SESSION);
    }
}
