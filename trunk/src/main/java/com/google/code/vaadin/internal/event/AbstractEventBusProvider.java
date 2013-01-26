/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.EventBus;
import net.engio.mbassy.BusConfiguration;
import net.engio.mbassy.MBassador;

import javax.inject.Provider;
import javax.validation.constraints.NotNull;

/**
 * AbstractEventBusProvider - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
public abstract class AbstractEventBusProvider implements Provider<EventBus> {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public EventBus get() {
        BusConfiguration busConfiguration = getConfiguration();
        if (busConfiguration == null) {
            busConfiguration = BusConfiguration.Default();
        }

        busConfiguration = mapAnnotations(busConfiguration);
        final MBassador mBassador = new MBassador(busConfiguration);
        return new EventBus() {
            @Override
            public void subscribe(@NotNull Object subscriber) {
                mBassador.subscribe(subscriber);
            }

            @Override
            public void unsubscribe(@NotNull Object subscriber) {
                mBassador.unsubscribe(subscriber);
            }

            @Override
            public void publish(@NotNull Object event) {
                mBassador.publish(event);
            }
        };
    }

    protected abstract BusConfiguration getConfiguration();

    protected BusConfiguration mapAnnotations(BusConfiguration busConfiguration) {
        busConfiguration.setMetadataReader(new CompositeMetadataReader());
        return busConfiguration;
    }
}
