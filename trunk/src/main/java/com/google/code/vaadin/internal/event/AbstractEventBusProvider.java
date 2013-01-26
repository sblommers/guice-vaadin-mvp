/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import net.engio.mbassy.BusConfiguration;
import net.engio.mbassy.IMessageBus;
import net.engio.mbassy.MBassador;

import javax.inject.Provider;

/**
 * AbstractEventBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public abstract class AbstractEventBusProvider implements Provider<IMessageBus> {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public IMessageBus get() {
        BusConfiguration busConfiguration = getConfiguration();
        if (busConfiguration == null) {
            busConfiguration = BusConfiguration.Default();
        }

        busConfiguration = mapAnnotations(busConfiguration);
        return new MBassador(busConfiguration);
    }

    protected abstract BusConfiguration getConfiguration();

    protected BusConfiguration mapAnnotations(BusConfiguration busConfiguration) {
        busConfiguration.setMetadataReader(new CompositeMetadataReader());
        return busConfiguration;
    }
}
