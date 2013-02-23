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

package com.google.code.vaadin.internal.eventhandling.sharedmodel;

import com.google.code.vaadin.internal.eventhandling.AbstractEventBusModule;
import com.google.code.vaadin.internal.eventhandling.EventBusTypeAutoSubscriber;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import com.google.code.vaadin.mvp.eventhandling.SharedModelEventPublisher;
import com.google.inject.Provider;
import com.google.inject.Scopes;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * SharedModelEventBusModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 14.02.13
 */
public class SharedModelEventBusModule extends AbstractEventBusModule {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusBinding eventBusBinding;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public SharedModelEventBusModule(EventBusBinding eventBusBinding) {
        this.eventBusBinding = eventBusBinding;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(SharedEventBusSubscribersRegistry.class).to(AccessibleSharedEventBusSubscribersRegistry.class);
        bind(AccessibleSharedEventBusSubscribersRegistry.class).in(Scopes.SINGLETON);
        bindSharedModelEventBus();
    }

    protected void bindSharedModelEventBus() {
        bindEventBus(EventBusTypes.SHARED_MODEL,
                mapObservesAnnotations(EventBusTypes.SHARED_MODEL, eventBusBinding.getConfiguration()));

        bind(SharedModelEventPublisher.class).toProvider(new Provider<SharedModelEventPublisher>() {
            @Inject
            @EventBusType(EventBusTypes.SHARED_MODEL)
            private SharedModelEventPublisher publisher;

            @Override
            public SharedModelEventPublisher get() {
                return new SharedModelEventPublisher() {
                    @Override
                    public void publish(@NotNull Object event) {
                        publisher.publish(event);
                    }
                };
            }
        });
    }

    @Override
    protected EventBusTypeAutoSubscriber createEventBusTypeAutoSubscriber(EventBus eventBus, EventBusTypes eventBusType) {
        return new SharedModelEventBusTypeAutoSubscriber(eventBus, eventBusType);
    }
}