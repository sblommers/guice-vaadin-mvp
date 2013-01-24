/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.guice.event;

import com.google.code.vaadin.mvp.event.EventPublisher;
import com.google.common.base.Preconditions;
import com.google.inject.AbstractModule;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.InjectionListener;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.engio.mbassy.BusConfiguration;
import net.engio.mbassy.IMessageBus;
import net.engio.mbassy.MBassador;

/**
 * EventPublisherModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 24.01.13
 */
public class EventPublisherModule extends AbstractModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private BusConfiguration busConfiguration;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public EventPublisherModule() {
        busConfiguration = BusConfiguration.Default();
    }

    public EventPublisherModule(BusConfiguration busConfiguration) {
        Preconditions.checkArgument(busConfiguration != null, "BusConfiguration can't be null", busConfiguration);
        this.busConfiguration = busConfiguration;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        busConfiguration = mapAnnotations(busConfiguration);
        final MBassador eventBus = new MBassador(busConfiguration);

        // Registers all injectees as EventBus subscribers because we can't definitely say who is listening
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                typeEncounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        eventBus.subscribe(injectee);
                    }
                });
            }
        });

        bind(IMessageBus.class).toInstance(eventBus);
        bind(MBassador.class).toInstance(eventBus);

        bind(EventPublisher.class).toInstance(new EventPublisher() {
            @Override
            public void publish(Object event) {
                eventBus.publish(event);
            }
        });
    }

    protected BusConfiguration mapAnnotations(BusConfiguration busConfiguration) {
        busConfiguration.setMetadataReader(new CompositeMetadataReader());
        return busConfiguration;
    }

}
