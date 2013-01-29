/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.publisher;

import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.EventBuses;
import com.google.code.vaadin.mvp.ModelEventPublisher;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * ModelEventPublisherProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class ModelEventPublisherProvider implements Provider<ModelEventPublisher> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Logger logger;
    private EventBus modelEventBus;

    /*===========================================[ CONSTRUCTORS ]=================*/

    @Inject
    public void init(Logger logger, @EventBuses.ModelEventBus EventBus modelEventBus) {
        this.logger = logger;
        this.modelEventBus = modelEventBus;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public ModelEventPublisher get() {
        ModelEventPublisher modelEventPublisher = new ModelEventPublisher() {
            @Override
            public void publish(Object event) {
                modelEventBus.publish(event);
            }
        };

        logger.debug(String.format("ModelEventPublisher created: [%d]", modelEventBus.hashCode()));
        return modelEventPublisher;
    }
}