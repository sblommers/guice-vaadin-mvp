/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.configuration;

import net.engio.mbassy.BusConfiguration;

/**
 *
 * @author Alexey Krylov (lexx)
 * @since 20.02.13
 */
class AccessibleEventBusBinding implements EventBusBinding {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusTypes type;
    private BusConfiguration configuration;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public EventBusTypes getType() {
        return type;
    }

    @Override
    public BusConfiguration getConfiguration() {
        return configuration;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    protected void setType(EventBusTypes type) {
        this.type = type;
    }

    protected void setConfiguration(BusConfiguration configuration) {
        this.configuration = configuration;
    }
}
