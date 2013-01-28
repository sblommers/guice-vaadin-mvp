/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.publisher;

import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.EventBuses;
import com.google.code.vaadin.mvp.GlobalModelEventPublisher;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * ModelEventPublisherProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class GlobalModelEventPublisherProvider implements Provider<GlobalModelEventPublisher> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Logger logger;
    private EventBus globalModelEventBus;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    public void init(Logger logger, @EventBuses.GlobalModelEventBus EventBus globalModelEventBus) {
        this.logger = logger;
        this.globalModelEventBus = globalModelEventBus;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public GlobalModelEventPublisher get() {
        GlobalModelEventPublisher modelEventPublisher = new GlobalModelEventPublisher() {
            @Override
            public void publish(Object event) {
                globalModelEventBus.publish(event);
            }
        };

        logger.debug(String.format("GlobalModelEventPublisher created: [%d]", globalModelEventBus.hashCode()));
        return modelEventPublisher;
    }
}
