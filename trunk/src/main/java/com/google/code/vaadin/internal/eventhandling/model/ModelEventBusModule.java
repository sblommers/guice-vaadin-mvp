/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.model;

import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.code.vaadin.mvp.eventhandling.ModelEventPublisher;
import com.google.inject.AbstractModule;
import net.engio.mbassy.IMessageBus;

/**
 * ModelEventBusModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class ModelEventBusModule extends AbstractModule {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(EventBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelEventBusProvider.class).in(UIScope.getCurrent());
        bind(IMessageBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelMessageBusProvider.class).in(UIScope.getCurrent());
        bind(ModelEventPublisher.class).toProvider(ModelEventPublisherProvider.class).in(UIScope.getCurrent());
    }
}
