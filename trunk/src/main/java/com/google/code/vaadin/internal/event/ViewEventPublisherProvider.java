/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.ViewEventPublisher;
import com.google.common.base.Preconditions;
import net.engio.mbassy.IMessageBus;

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

    @Inject
    @EventPublisherModule.ViewEventBus
    private IMessageBus viewMessageBus;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public ViewEventPublisher get() {
        return new ViewEventPublisher() {
            @Override
            public void publish(Object event) {
                Preconditions.checkArgument(event != null, "Published Event can't be null");
                viewMessageBus.post(event).now();
            }
        };
    }
}
