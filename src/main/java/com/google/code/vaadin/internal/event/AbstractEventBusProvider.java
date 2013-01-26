/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.EventBus;
import net.engio.mbassy.IMessageBus;

import javax.inject.Provider;
import javax.validation.constraints.NotNull;

/**
 * AbstractEventBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public abstract class AbstractEventBusProvider implements Provider<EventBus> {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public EventBus get() {
        final IMessageBus bus = getMessageBus();
        return new EventBus() {
            @Override
            public void subscribe(@NotNull Object subscriber) {
                bus.subscribe(subscriber);
            }

            @Override
            public void unsubscribe(@NotNull Object subscriber) {
                bus.unsubscribe(subscriber);
            }

            @Override
            public void publish(@NotNull Object event) {
                bus.post(event).now();
            }
        };
    }

    public abstract IMessageBus getMessageBus();
}
