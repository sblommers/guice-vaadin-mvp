/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.publisher;

import com.google.code.vaadin.internal.event.EventBusModule;
import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.ViewEventPublisher;
import org.slf4j.Logger;

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

    private Logger logger;
    private EventBus viewMessageBus;
    private EventBus globalViewMessageBus;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Inject
    public void init(Logger logger, @EventBusModule.ViewEventBus EventBus viewMessageBus,
                     @EventBusModule.GlobalViewEventBus EventBus globalViewMessageBus) {
        this.logger = logger;
        this.viewMessageBus = viewMessageBus;
        this.globalViewMessageBus = globalViewMessageBus;
    }

    @Override
    public ViewEventPublisher get() {
        ViewEventPublisher viewEventPublisher = new ViewEventPublisher() {
            @Override
            public void publish(Object event) {
                viewMessageBus.publish(event);
                globalViewMessageBus.publish(event);
            }
        };

        logger.debug(String.format("ViewEventPublisher created: [%d], global: [%d]", viewMessageBus.hashCode(), globalViewMessageBus.hashCode()));
        return viewEventPublisher;
    }
}
