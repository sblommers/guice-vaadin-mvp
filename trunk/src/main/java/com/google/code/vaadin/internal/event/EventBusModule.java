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
import com.google.code.vaadin.internal.event.eventbus.GlobalModelEventBusProvider;
import com.google.code.vaadin.internal.event.eventbus.ModelEventBusProvider;
import com.google.code.vaadin.internal.event.eventbus.ViewEventBusProvider;
import com.google.code.vaadin.internal.event.messagebus.GlobalModelMessageBusProvider;
import com.google.code.vaadin.internal.event.messagebus.ModelMessageBusProvider;
import com.google.code.vaadin.internal.event.messagebus.ViewMessageBusProvider;
import com.google.code.vaadin.internal.event.publisher.GlobalModelEventPublisherProvider;
import com.google.code.vaadin.internal.event.publisher.ModelEventPublisherProvider;
import com.google.code.vaadin.internal.event.publisher.ViewEventPublisherProvider;
import com.google.code.vaadin.mvp.*;
import com.google.inject.AbstractModule;
import com.google.inject.Injector;
import com.google.inject.Scopes;
import com.google.inject.TypeLiteral;
import com.google.inject.matcher.Matchers;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;
import net.engio.mbassy.IMessageBus;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * EventPublisherModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class EventBusModule extends AbstractModule {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(EventBusSubscribersRegistry.class).to(DefaultEventBusSubscribersRegistry.class).in(Scopes.SINGLETON);

        // Registers all injectees as EventBus subscribers because we can't definitely say who is listening
        EventBusTypeListener eventBusTypeListener = new EventBusTypeListener();
        requestInjection(eventBusTypeListener);
        bindListener(Matchers.any(), eventBusTypeListener);


        // MessageBus
        bindMessageBus();
        // EventBus
        bindEventBus();
        // EventPublishers
        bindEventPublishers();
    }

    private void bindMessageBus() {
        bind(IMessageBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelMessageBusProvider.class).in(UIScope.getCurrent());
        bind(IMessageBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewMessageBusProvider.class).in(UIScope.getCurrent());
        bind(IMessageBus.class).annotatedWith(EventBuses.GlobalModelEventBus.class).toProvider(GlobalModelMessageBusProvider.class).in(Scopes.SINGLETON);
    }

    private void bindEventBus() {
        bind(EventBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelEventBusProvider.class).in(UIScope.getCurrent());
        bind(EventBus.class).annotatedWith(EventBuses.ViewEventBus.class).toProvider(ViewEventBusProvider.class).in(UIScope.getCurrent());
        bind(EventBus.class).annotatedWith(EventBuses.GlobalModelEventBus.class).toProvider(GlobalModelEventBusProvider.class).in(Scopes.SINGLETON);
    }

    private void bindEventPublishers() {
        bind(GlobalModelEventPublisher.class).toProvider(GlobalModelEventPublisherProvider.class).in(Scopes.SINGLETON);
        bind(ModelEventPublisher.class).toProvider(ModelEventPublisherProvider.class).in(UIScope.getCurrent());
        bind(ViewEventPublisher.class).toProvider(ViewEventPublisherProvider.class).in(UIScope.getCurrent());
    }

    private static class EventBusTypeListener implements TypeListener {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

        @Inject
        private Provider<Injector> injectorProvider;

	/*===========================================[ INTERFACE METHODS ]============*/

        @Override
        public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
            if (injectorProvider != null) {
                encounter.register(new EventPublisherInjector<I>(injectorProvider.get()));
            }
        }
    }
}
