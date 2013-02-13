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

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.internal.event.configuration.DefaultEventBusModuleConfigurationBuilder;
import com.google.code.vaadin.internal.event.eventbus.SharedModelEventBusProvider;
import com.google.code.vaadin.internal.event.eventbus.ModelEventBusProvider;
import com.google.code.vaadin.internal.event.eventbus.ViewEventBusProvider;
import com.google.code.vaadin.internal.event.messagebus.SharedMessageBusProvider;
import com.google.code.vaadin.internal.event.messagebus.MessageBusProvider;
import com.google.code.vaadin.internal.event.messagebus.ViewMessageBusProvider;
import com.google.code.vaadin.internal.event.publisher.SharedModelEventPublisherProvider;
import com.google.code.vaadin.internal.event.publisher.ModelEventPublisherProvider;
import com.google.code.vaadin.internal.event.publisher.ViewEventPublisherProvider;
import com.google.code.vaadin.mvp.*;
import com.google.inject.AbstractModule;
import com.google.inject.Scopes;
import com.google.inject.matcher.Matchers;
import net.engio.mbassy.IMessageBus;

/**
 * EventPublisherModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class EventBusModule extends AbstractModule {

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
        bind(EventBusSubscribersRegistry.class).to(DefaultEventBusSubscribersRegistry.class).in(Scopes.SINGLETON);

        // Registers all injectees as EventBus subscribers because we can't definitely say who is listening
        EventBusTypeListener eventBusTypeListener = new EventBusTypeListener(configuration);
        requestInjection(eventBusTypeListener);
        bindListener(Matchers.any(), eventBusTypeListener);

        bindViewEventBus();

        if (configuration.isModelEventBusRequired()) {
            bindModelEventBus();
        }

        if (configuration.isSharedModelEventBusRequired()) {
            bindSharedModelEventBus();
        }
    }

    protected void bindViewEventBus() {
        bind(EventBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewEventBusProvider.class).in(UIScope.getCurrent());
        bind(IMessageBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewMessageBusProvider.class).in(UIScope.getCurrent());
        bind(ViewEventPublisher.class).toProvider(ViewEventPublisherProvider.class).in(UIScope.getCurrent());
    }

    protected void bindModelEventBus() {
        bind(EventBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelEventBusProvider.class).in(UIScope.getCurrent());
        bind(IMessageBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(MessageBusProvider.class).in(UIScope.getCurrent());
        bind(ModelEventPublisher.class).toProvider(ModelEventPublisherProvider.class).in(UIScope.getCurrent());
    }

    protected void bindSharedModelEventBus() {
        bind(EventBus.class).annotatedWith(EventBuses.SharedModelEventBus.class).toProvider(SharedModelEventBusProvider.class).in(Scopes.SINGLETON);
        bind(IMessageBus.class).annotatedWith(EventBuses.SharedModelEventBus.class).toProvider(SharedMessageBusProvider.class).in(Scopes.SINGLETON);
        bind(SharedModelEventPublisher.class).toProvider(SharedModelEventPublisherProvider.class).in(Scopes.SINGLETON);
    }
}
