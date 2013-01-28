/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.eventbus;

import com.google.code.vaadin.internal.event.AbstractEventBusProvider;
import com.google.code.vaadin.internal.event.messagebus.ViewMessageBusProvider;
import net.engio.mbassy.IMessageBus;

import javax.inject.Inject;

/**
 * ViewEventBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class ViewEventBusProvider extends AbstractEventBusProvider {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private ViewMessageBusProvider messageBusProvider;

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public IMessageBus getMessageBus() {
        return messageBusProvider.get();
    }
}
