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

import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import com.google.code.vaadin.mvp.eventhandling.EventPublisher;
import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.inject.AbstractModule;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeListener;
import net.engio.mbassy.BusConfiguration;
import net.engio.mbassy.IMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;

/**
 * AbstractEventBusModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.02.13
 */
public abstract class AbstractEventBusModule extends AbstractModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    protected Logger logger;
    protected EventBusBinding eventBusBinding;

    /*===========================================[ CONSTRUCTORS ]=================*/

    protected AbstractEventBusModule(EventBusBinding eventBusBinding) {
        logger = LoggerFactory.getLogger(getClass());
        this.eventBusBinding = eventBusBinding;
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void configure() {
        BusConfiguration busConfiguration = eventBusBinding.getConfiguration();
        EventBusTypes busType = eventBusBinding.getType();

        busConfiguration.setMetadataReader(createMetadataReader(busType.getEventType()));
        bind(BusConfiguration.class).annotatedWith(eventBusType(busType)).toInstance(busConfiguration);
        bindEventBus();
    }

    protected CompositeMetadataReader createMetadataReader(EventType eventType) {
        return new CompositeMetadataReader(eventType);
    }

    protected void bindEventBus() {
        Annotation eventBusType = eventBusType(eventBusBinding.getType());
        Scope bindScope = getBindScope();
        bind(IMessageBus.class).annotatedWith(eventBusType).toProvider(getMessageBusProviderClass()).in(bindScope);
        bind(EventBus.class).annotatedWith(eventBusType).toProvider(getEventBusProviderClass()).in(bindScope);
        bind(EventPublisher.class).annotatedWith(eventBusType).toProvider(getEventBusProviderClass()).in(bindScope);
        bindSpecificEventPublisher();
        bindAutoSubscriber();
    }

    protected abstract Scope getBindScope();

    protected abstract Class<? extends Provider<? extends IMessageBus>> getMessageBusProviderClass();

    protected abstract Class<? extends Provider<? extends EventBus>> getEventBusProviderClass();

    protected abstract void bindSpecificEventPublisher();

    public static EventBusType eventBusType(EventBusTypes type) {
        return new EventBusTypeImpl(type);
    }

    protected void bindAutoSubscriber() {
        TypeListener eventBusTypeAutoSubscriber = createEventBusTypeAutoSubscriber(getEventBusProviderClass(), eventBusBinding.getType().getEventType());
        requestInjection(eventBusTypeAutoSubscriber);
        bindListener(Matchers.any(), eventBusTypeAutoSubscriber);
    }

    protected EventBusTypeAutoSubscriber createEventBusTypeAutoSubscriber(Class<? extends Provider<? extends EventBus>> eventBusProviderClass, EventType eventType) {
        return new EventBusTypeAutoSubscriber(eventBusProviderClass, eventType);
    }
}
