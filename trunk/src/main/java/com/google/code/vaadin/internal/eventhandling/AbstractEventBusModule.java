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

import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import com.google.code.vaadin.mvp.eventhandling.EventPublisher;
import com.google.inject.AbstractModule;
import com.google.inject.Key;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeListener;
import net.engio.mbassy.BusConfiguration;
import net.engio.mbassy.IMessageBus;
import net.engio.mbassy.MBassador;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * AbstractEventBusModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.02.13
 */
public abstract class AbstractEventBusModule extends AbstractModule {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Logger logger;

	/*===========================================[ CONSTRUCTORS ]=================*/

    protected AbstractEventBusModule() {
        logger = LoggerFactory.getLogger(getClass());
    }

	/*===========================================[ CLASS METHODS ]================*/

    protected BusConfiguration mapObservesAnnotations(EventBusTypes busType, BusConfiguration busConfiguration) {
        busConfiguration.setMetadataReader(new CompositeMetadataReader(busType));
        return busConfiguration;
    }

    protected void bindEventBus(EventBusTypes type, BusConfiguration configuration) {
        IMessageBus eventBus = new MBassador(configuration);
        EventBus bus = createEventBus(eventBus);
        requestInjection(bus);

        EventBusType eventBusType = eventBusType(type);
        bind(IMessageBus.class).annotatedWith(eventBusType).toInstance(eventBus);
        bind(EventPublisher.class).annotatedWith(eventBusType).to(Key.get(EventBus.class, eventBusType));
        bind(EventBus.class).annotatedWith(eventBusType).toInstance(bus);
        bindAutoSubscriber(bus, type);

        logger.debug(String.format("EventBus for [%s] created: [%d]", type, eventBus.hashCode()));
    }

    protected EventBus createEventBus(IMessageBus messageBus) {
        return new DefaultEventBus(messageBus);
    }

    public static EventBusType eventBusType(EventBusTypes type) {
        return new EventBusTypeImpl(type);
    }

    protected void bindAutoSubscriber(EventBus eventBus, EventBusTypes eventBusType) {
        TypeListener eventBusTypeAutoSubscriber = createEventBusTypeAutoSubscriber(eventBus, eventBusType);
        requestInjection(eventBusTypeAutoSubscriber);
        bindListener(Matchers.any(), eventBusTypeAutoSubscriber);
    }

    protected EventBusTypeAutoSubscriber createEventBusTypeAutoSubscriber(EventBus eventBus, EventBusTypes eventBusType) {
        return new EventBusTypeAutoSubscriber(eventBus, eventBusType);
    }
}
