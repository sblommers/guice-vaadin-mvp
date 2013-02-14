/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling;

import com.google.code.vaadin.internal.eventhandling.configuration.EventBusModuleConfiguration;
import com.google.code.vaadin.internal.eventhandling.model.ModelEventBusSubscriber;
import com.google.code.vaadin.internal.eventhandling.sharedmodel.SharedModelEventBusSubscriber;
import com.google.code.vaadin.internal.eventhandling.view.ViewEventBusSubscriber;
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
class EventBusTypeAutoSubscriber implements TypeListener {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private Provider<Injector> injectorProvider;
    private EventBusModuleConfiguration configuration;

	/*===========================================[ CONSTRUCTORS ]=================*/

    EventBusTypeAutoSubscriber(EventBusModuleConfiguration configuration) {
        this.configuration = configuration;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    /**
     * Auto-subscription will only work if subscriber is directly injected by someone and this someone is not in
     * Singleton scope.
     */
    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        // This check will skip all Singleton instantiations
        if (injectorProvider != null) {
            Injector injector = injectorProvider.get();
            encounter.register(new ViewEventBusSubscriber<I>(injector));

            if (configuration.isModelEventBusRequired()) {
                encounter.register(new ModelEventBusSubscriber<I>(injector));
            }

            if (configuration.isSharedModelEventBusRequired()) {
                encounter.register(new SharedModelEventBusSubscriber<I>(injector));
            }
        }
    }
}
