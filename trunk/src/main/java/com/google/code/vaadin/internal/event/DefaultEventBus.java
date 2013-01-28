/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.EventBus;
import com.google.common.base.Preconditions;
import net.engio.mbassy.IMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * DefaultAccessibleEventBus - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
class DefaultEventBus implements EventBus {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(DefaultEventBus.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private IMessageBus bus;

    /*===========================================[ CONSTRUCTORS ]=================*/

    DefaultEventBus(IMessageBus bus) {
        this.bus = bus;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

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
        Preconditions.checkArgument(event != null, "Published Event can't be null");
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Event: [%s] -> %d", event, hashCode()));
        }
        bus.post(event).now();
    }
}
