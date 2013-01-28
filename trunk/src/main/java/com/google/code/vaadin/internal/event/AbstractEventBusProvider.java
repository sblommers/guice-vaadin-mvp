/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.EventBus;
import net.engio.mbassy.IMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.inject.Provider;

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
        Logger logger = LoggerFactory.getLogger(getClass());
        EventBus eventBus = createEventBus();
        logger.debug("EventBus created: " + eventBus.hashCode());
        return eventBus;
    }

    protected EventBus createEventBus() {
        return new DefaultEventBus(getMessageBus());
    }

    public abstract IMessageBus getMessageBus();

}
