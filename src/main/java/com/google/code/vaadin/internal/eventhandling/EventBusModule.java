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

import com.google.code.vaadin.internal.eventhandling.configuration.DefaultEventBusBinder;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinder;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.internal.eventhandling.model.ModelEventBusModule;
import com.google.code.vaadin.internal.eventhandling.sharedmodel.SharedModelEventBusModule;
import com.google.code.vaadin.internal.eventhandling.view.ViewEventBusModule;
import com.google.inject.AbstractModule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventPublisherModule - TODO: description
 *
 * @author Alexey Krylov
 * @since 24.01.13
 */
public class EventBusModule extends AbstractModule{

	/*===========================================[ STATIC VARIABLES ]=============*/

    protected static final Logger logger = LoggerFactory.getLogger(EventBusModule.class);

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        String simpleName = getClass().getSimpleName();
        String moduleName = simpleName.isEmpty() ? getClass().getName() : simpleName;
        logger.info(String.format("Configuring [%s]", moduleName));

        EventBusBinder eventBusBinder = createEventBusBinder();
        bind(EventBusBinder.class).toInstance(eventBusBinder);

        bindEventBuses(eventBusBinder);

        EventBusBinding viewEventBusBinding = eventBusBinder.getBinding(EventBusTypes.VIEW);
        if (viewEventBusBinding == null) {
            eventBusBinder.bind(EventBusTypes.VIEW).withDefaultConfiguration();
            viewEventBusBinding = eventBusBinder.getBinding(EventBusTypes.VIEW);
            logger.info("Using default configuration for View EventBus");
        }

        install(new ViewEventBusModule(viewEventBusBinding));

        EventBusBinding modelEventBusBinding = eventBusBinder.getBinding(EventBusTypes.MODEL);
        if (modelEventBusBinding != null) {
            install(new ModelEventBusModule(modelEventBusBinding));
        }

        EventBusBinding sharedModelEventBusBinding = eventBusBinder.getBinding(EventBusTypes.SHARED_MODEL);
        if (sharedModelEventBusBinding != null) {
            install(new SharedModelEventBusModule(sharedModelEventBusBinding));
        }

        logger.info(String.format("[%s] configured", moduleName));
    }

    protected EventBusBinder createEventBusBinder() {
        return new DefaultEventBusBinder();
    }

    protected void bindEventBuses(EventBusBinder binder) {
    }
}