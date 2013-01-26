/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.publisher;

import com.google.code.vaadin.internal.event.EventBusModule;
import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.ModelEventPublisher;

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

    @Inject
    @EventBusModule.ModelEventBus
    private EventBus modelMessageBus;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public ModelEventPublisher get() {
        return new ModelEventPublisher() {
            @Override
            public void publish(Object event) {
                modelMessageBus.publish(event);
            }
        };
    }
}
