/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.eventhandling.configuration;

import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import net.engio.mbassy.BusConfiguration;

/**
 *
 * @author Alexey Krylov (lexx)
 * @since 20.02.13
 */
class AccessibleEventBusBinding implements EventBusBinding {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusType type;
    private BusConfiguration configuration;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public EventBusType getType() {
        return type;
    }

    @Override
    public BusConfiguration getConfiguration() {
        return configuration;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    protected void setType(EventBusType type) {
        this.type = type;
    }

    protected void setConfiguration(BusConfiguration configuration) {
        this.configuration = configuration;
    }
}
