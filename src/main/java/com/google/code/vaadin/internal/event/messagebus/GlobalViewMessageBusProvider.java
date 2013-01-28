/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event.messagebus;

import com.google.code.vaadin.internal.event.AbstractMessageBusProvider;
import com.google.code.vaadin.mvp.EventBuses;
import com.google.inject.Inject;
import net.engio.mbassy.BusConfiguration;

/**
 * GlobalViewMessageBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class GlobalViewMessageBusProvider extends AbstractMessageBusProvider {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject(optional = true)
    @EventBuses.GlobalViewEventBus
    private BusConfiguration busConfiguration;

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected BusConfiguration getConfiguration() {
        return busConfiguration;
    }
}
