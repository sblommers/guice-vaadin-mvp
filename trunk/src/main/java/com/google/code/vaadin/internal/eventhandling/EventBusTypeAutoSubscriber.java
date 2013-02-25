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

import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.inject.Injector;
import com.google.inject.Provider;
import com.google.inject.TypeLiteral;
import com.google.inject.spi.TypeEncounter;
import com.google.inject.spi.TypeListener;

import javax.inject.Inject;

/**
 * @author Alexey Krylov
 * @since 13.02.13
 */
public class EventBusTypeAutoSubscriber implements TypeListener {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    protected Injector injector;

    protected Class<? extends Provider<? extends EventBus>> eventBusProviderClass;
    protected EventType eventType;

    /*===========================================[ CONSTRUCTORS ]=================*/

    protected EventBusTypeAutoSubscriber(Class<? extends Provider<? extends EventBus>> eventBusProviderClass, EventType eventType) {
        this.eventBusProviderClass = eventBusProviderClass;
        this.eventType = eventType;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public <I> void hear(TypeLiteral<I> type, TypeEncounter<I> encounter) {
        if (injector != null) {
            encounter.register(createEventBusSubscriber(injector.getInstance(eventBusProviderClass).get()));
        }
    }

    protected EventBusSubscriber<Object> createEventBusSubscriber(EventBus eventBus) {
        return new EventBusSubscriber<>(eventBus, eventType);
    }
}