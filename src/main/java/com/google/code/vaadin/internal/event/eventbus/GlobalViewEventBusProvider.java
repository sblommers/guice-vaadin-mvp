/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.eventbus;

import com.google.code.vaadin.internal.event.AbstractEventBusProvider;
import com.google.code.vaadin.internal.event.messagebus.GlobalViewMessageBusProvider;
import net.engio.mbassy.IMessageBus;

import javax.inject.Inject;

/**
 * GlobalViewEventBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class GlobalViewEventBusProvider extends AbstractEventBusProvider {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    private GlobalViewMessageBusProvider messageBusProvider;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public IMessageBus getMessageBus() {
        return messageBusProvider.get();
    }
}
