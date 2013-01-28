/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.publisher;

import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.EventBuses;
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
    private EventBus viewEventBus;
    private EventBus globalViewEventBus;

    /*===========================================[ INTERFACE METHODS ]============*/

    @Inject
    public void init(Logger logger, @EventBuses.ViewEventBus EventBus viewEventBus,
                     @EventBuses.GlobalViewEventBus EventBus globalViewEventBus) {
        this.logger = logger;
        this.viewEventBus = viewEventBus;
        this.globalViewEventBus = globalViewEventBus;
    }

    @Override
    public ViewEventPublisher get() {
        ViewEventPublisher viewEventPublisher = new ViewEventPublisher() {
            @Override
            public void publish(Object event) {
                viewEventBus.publish(event);
                globalViewEventBus.publish(event);
            }
        };

        logger.debug(String.format("ViewEventPublisher created: [%d], global: [%d]", viewEventBus.hashCode(), globalViewEventBus.hashCode()));
        return viewEventPublisher;
    }
}
