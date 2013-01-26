/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.inject.Inject;
import net.engio.mbassy.BusConfiguration;

/**
 * ModelEventBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public class ModelEventBusProvider extends AbstractEventBusProvider {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject(optional = true)
    @EventBusModule.ModelEventBus
    private BusConfiguration busConfiguration;

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected BusConfiguration getConfiguration() {
        return busConfiguration;
    }
}