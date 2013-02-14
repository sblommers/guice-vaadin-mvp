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

import com.google.code.vaadin.internal.eventhandling.configuration.DefaultEventBusModuleConfigurationBuilder;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusModuleConfiguration;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusModuleConfigurationBuilder;
import com.google.code.vaadin.internal.eventhandling.model.ModelEventBusModule;
import com.google.code.vaadin.internal.eventhandling.sharedmodel.SharedModelEventBusModule;
import com.google.code.vaadin.internal.eventhandling.view.ViewEventBusModule;
import com.google.inject.AbstractModule;
import com.google.inject.matcher.Matchers;

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
        bind(EventBusModuleConfiguration.class).toInstance(configuration);

        // Registers all injectees as EventBus subscribers because we can't definitely say who is listening
        EventBusTypeListener eventBusTypeListener = new EventBusTypeListener(configuration);
        requestInjection(eventBusTypeListener);
        bindListener(Matchers.any(), eventBusTypeListener);

        install(new ViewEventBusModule());

        if (configuration.isModelEventBusRequired()) {
            install(new ModelEventBusModule());
        }
        if (configuration.isSharedModelEventBusRequired()) {
            install(new SharedModelEventBusModule());
        }
    }
}
