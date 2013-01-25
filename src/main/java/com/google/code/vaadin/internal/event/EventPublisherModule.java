/*
 * Copyright (c) 2013, i-Free. All Rights Reserved.
 * Use is subject to license terms.
 */

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.ViewEventPublisher;
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
 * @author Alexey Krylov
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
        final MBassador viewEventBus = new MBassador(busConfiguration);

        // Registers all injectees as EventBus subscribers because we can't definitely say who is listening
        bindListener(Matchers.any(), new TypeListener() {
            @Override
            public <I> void hear(TypeLiteral<I> typeLiteral, TypeEncounter<I> typeEncounter) {
                typeEncounter.register(new InjectionListener<I>() {
                    @Override
                    public void afterInjection(I injectee) {
                        viewEventBus.subscribe(injectee);
                    }
                });
            }
        });

        bind(IMessageBus.class).toInstance(viewEventBus);
        bind(MBassador.class).toInstance(viewEventBus);
        bind(ViewEventPublisher.class).toInstance(new ViewEventPublisher() {
            @Override
            public void publish(Object event) {
                Preconditions.checkArgument(event != null, "Published Event can't be null");
                viewEventBus.publish(event);
            }
        });
        //bindInterceptor(Matchers.any(), Matchers.annotatedWith(Observes.class).or(Matchers.annotatedWith(Listener.class)), new ViewEventPublisherRegistrar(viewEventBus));
        //TODO: bind modeleventpublisher separately
    }

    protected BusConfiguration mapAnnotations(BusConfiguration busConfiguration) {
        busConfiguration.setMetadataReader(new CompositeMetadataReader());
        return busConfiguration;
    }
}