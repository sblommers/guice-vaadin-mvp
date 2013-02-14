/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.sharedmodel;

import com.google.code.vaadin.internal.eventhandling.SharedEventBusSubscribersRegistry;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.code.vaadin.mvp.eventhandling.SharedModelEventPublisher;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import net.engio.mbassy.IMessageBus;

/**
 * SharedModelEventBusModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class SharedModelEventBusModule extends AbstractModule {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(SharedEventBusSubscribersRegistry.class).to(DefaultSharedEventBusSubscribersRegistry.class).in(Scopes.SINGLETON);
        bind(EventBus.class).annotatedWith(EventBuses.SharedModelEventBus.class).toProvider(SharedModelEventBusProvider.class).in(Scopes.SINGLETON);
        bind(IMessageBus.class).annotatedWith(EventBuses.SharedModelEventBus.class).toProvider(SharedMessageBusProvider.class).in(Scopes.SINGLETON);
        bind(SharedModelEventPublisher.class).toProvider(SharedModelEventPublisherProvider.class).in(Scopes.SINGLETON);
    }
}
