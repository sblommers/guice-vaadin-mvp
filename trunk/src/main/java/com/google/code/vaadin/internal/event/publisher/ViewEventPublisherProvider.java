/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.publisher;

import com.google.code.vaadin.internal.event.EventBusModule;
import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.ViewEventPublisher;
import com.google.common.base.Preconditions;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * ViewEventPublisherProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class ViewEventPublisherProvider implements Provider<ViewEventPublisher> {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBus viewMessageBus;
    private EventBus globalViewMessageBus;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Inject
    public void init(@EventBusModule.ViewEventBus EventBus viewMessageBus,
                     @EventBusModule.GlobalViewEventBus EventBus globalViewMessageBus) {
        this.viewMessageBus = viewMessageBus;
        this.globalViewMessageBus = globalViewMessageBus;
    }

    @Override
    public ViewEventPublisher get() {
        return new ViewEventPublisher() {
            @Override
            public void publish(Object event) {
                Preconditions.checkArgument(event != null, "Published Event can't be null");
                viewMessageBus.publish(event);
                globalViewMessageBus.publish(event);
            }
        };
    }
}
