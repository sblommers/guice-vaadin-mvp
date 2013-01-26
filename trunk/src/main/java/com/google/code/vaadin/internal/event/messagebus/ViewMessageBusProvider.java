/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.messagebus;

import com.google.code.vaadin.internal.event.AbstractMessageBusProvider;
import com.google.code.vaadin.internal.event.EventBusModule;
import com.google.inject.Inject;
import net.engio.mbassy.BusConfiguration;

/**
 * ViewMessageBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class ViewMessageBusProvider extends AbstractMessageBusProvider {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject(optional = true)
    @EventBusModule.ViewEventBus
    private BusConfiguration busConfiguration;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected BusConfiguration getConfiguration() {
        return busConfiguration;
    }
}