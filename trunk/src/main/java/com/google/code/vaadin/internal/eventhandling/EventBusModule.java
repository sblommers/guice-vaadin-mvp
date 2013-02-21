/*
 * Copyright (C) 2013 the original author or authors.
 * See the notice.md file distributed with this work for additional
 * information regarding copyright ownership.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.google.code.vaadin.internal.eventhandling;

import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinder;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.internal.eventhandling.model.ModelEventBusModule;
import com.google.code.vaadin.internal.eventhandling.sharedmodel.SharedModelEventBusModule;
import com.google.code.vaadin.internal.eventhandling.view.ViewEventBusModule;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventPublisher;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeListener;
import com.sun.javafx.util.MessageBus;
import net.engio.mbassy.BusConfiguration;
import net.engio.mbassy.IMessageBus;
import net.engio.mbassy.MBassador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.google.inject.name.Names.named;

/**
 * EventPublisherModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class EventBusModule extends AbstractModule {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(EventBusModule.class);

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusModuleConfiguration configuration;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public EventBusModule() {
        this(getConfigurationBuilder().build());
    }

    public EventBusModule(EventBusModuleConfiguration configuration) {
        this.configuration = configuration;
    }

	/*===========================================[ CLASS METHODS ]================*/

    public static EventBusModuleConfigurationBuilder getConfigurationBuilder() {
        return new DefaultEventBusModuleConfigurationBuilder();
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        String simpleName = getClass().getSimpleName();
        String moduleName = simpleName.isEmpty() ? getClass().getName() : simpleName;
        logger.info(String.format("Configuring [%s]", moduleName));

        bind(EventBusModuleConfiguration.class).toInstance(configuration);

        bindViewEventBus(createDefaultMessageBusConfiguration());
        EventBusBinder eventBusBinder = createEventBusBinder();
        bindEventBuses(eventBusBinder);

        EventBusBinding modelBusBinding = eventBusBinder.getBinding(EventBusTypes.MODEL);
        if (modelBusBinding != null) {
            //todo
        }


        EventBusBinding sharedModelBusBinding = eventBusBinder.getBinding(EventBusTypes.MODEL);
        if (sharedModelBusBinding!=null) {
            //todo
        }
        // Bind named Message Buses
        Iterable<EventBusBinding> bindings = eventBusBinder.getBindings();
        for (EventBusBinding binding : bindings) {
            EventBusTypes type = binding.getType();
            BusConfiguration configuration = mapObservesAnnotations(type, binding.getConfiguration());
            bindMessageBus(name, configuration);
        }

        logger.info(String.format("[%s] configured", moduleName));


        // Registers all injectees as EventBus subscribers because we can't definitely say who is listening
        TypeListener eventBusTypeAutoSubscriber = new EventBusTypeAutoSubscriber(configuration);
        requestInjection(eventBusTypeAutoSubscriber);
        bindListener(Matchers.any(), eventBusTypeAutoSubscriber);


        if (configuration.isSharedModelEventBusRequired()) {
            install(new SharedModelEventBusModule());
        }
        logger.info(String.format("[%s] configured", moduleName));
    }

    protected void bindViewEventBus(EventBusTypes eventBusType, BusConfiguration configuration) {
        mapObservesAnnotations(eventBusType, configuration);

        IMessageBus messageBus = new MBassador(configuration);
        MessageBus bus = createMessageBus(messageBus);
        requestInjection(bus);

        bind(IMessageBus.class).toInstance(messageBus);
        bind(EventPublisher.class).to(EventBus.class);
        bind(MessageBus.class).toInstance(bus);
        bindAutoSubscriber(messageBus, eventBusType);
        logger.debug("DefaultMessageBus created: " + messageBus.hashCode());
    }

    protected void bindMessageBus(String name, BusConfiguration configuration) {

        IMessageBus messageBus = new MBassador(configuration);
        MessageBus bus = createMessageBus(messageBus);
        requestInjection(bus);

        bind(IMessageBus.class).annotatedWith(named(name)).toInstance(messageBus);
        bind(MessagePublisher.class).annotatedWith(named(name)).to(Key.get(MessageBus.class, named(name)));
        bind(MessageBus.class).annotatedWith(named(name)).toInstance(bus);
        bindAutoSubscriber(messageBus, name);

        logger.debug("MessageBus created: " + messageBus.hashCode());
    }

    protected void bindAutoSubscriber(IMessageBus messageBus, EventBusTypes eventBusType) {
        TypeListener eventBusTypeAutoSubscriber = new EventBusTypeAutoSubscriber(messageBus, eventBusType);
        requestInjection(eventBusTypeAutoSubscriber);
        bindListener(Matchers.any(), eventBusTypeAutoSubscriber);
    }

    protected BusConfiguration createDefaultMessageBusConfiguration() {
        return BusConfiguration.Default();
    }

    protected EventBusBinder createEventBusBinder() {
        return new DefEventBusBi();
    }

    protected MessageBus createMessageBus(IMessageBus messageBus) {
        return new DefaultMessageBus(messageBus);
    }

    protected void bindEventBuses(EventBusBinder binder) {
    }

    protected BusConfiguration mapObservesAnnotations(EventBusTypes busType, BusConfiguration busConfiguration) {
        busConfiguration.setMetadataReader(new CompositeMetadataReader(busType));
        return busConfiguration;
    }

}
