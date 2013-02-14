/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.view;

import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.code.vaadin.mvp.eventhandling.ViewEventPublisher;
import com.google.inject.AbstractModule;
import net.engio.mbassy.IMessageBus;

/**
 * ViewEventBusModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class ViewEventBusModule extends AbstractModule {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(EventBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewEventBusProvider.class).in(UIScope.getCurrent());
        bind(IMessageBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewMessageBusProvider.class).in(UIScope.getCurrent());
        bind(ViewEventPublisher.class).toProvider(ViewEventPublisherProvider.class).in(UIScope.getCurrent());
    }
}
