/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.internal.event.configuration.EventBusModuleConfiguration;
import com.google.inject.Injector;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * EventBusTypeListener - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 13.02.13
 */
class EventBusTypeListener implements TypeListener {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private Provider<Injector> injectorProvider;
    private EventBusModuleConfiguration configuration;

	/*===========================================[ CONSTRUCTORS ]=================*/

    EventBusTypeListener(EventBusModuleConfiguration configuration) {
        this.configuration = configuration;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        if (injectorProvider != null) {
            encounter.register(new EventPublisherInjector<I>(configuration, injectorProvider.get()));
        }
    }
}
