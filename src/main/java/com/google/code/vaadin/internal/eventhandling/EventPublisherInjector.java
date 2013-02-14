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

import com.google.code.vaadin.internal.eventhandling.configuration.EventBusModuleConfiguration;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.inject.*;
import net.engio.mbassy.common.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * EventPublisherInjector - TODO: description
 *
 * @author Alexey Krylov
 * @since 26.01.13
 */
class EventPublisherInjector<T> implements MembersInjector<T> {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(EventPublisherInjector.class);

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusModuleConfiguration configuration;
    private Injector injector;

	/*===========================================[ CONSTRUCTORS ]=================*/

    EventPublisherInjector(EventBusModuleConfiguration configuration, Injector injector) {
        this.configuration = configuration;
        this.injector = injector;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        subscribeToViewEventBus(instance);

        if (configuration.isModelEventBusRequired()) {
            subscribeToModelEventBus(instance);
        }

        if (configuration.isSharedModelEventBusRequired()) {
            subscribeToSharedModelEventBus(instance);
        }
    }

    /**
     * Do not allowed to register Singleton-scoped instances as UI-scoped ViewEventBus subscriber
     * because there is no UIScope on Injector instantiation.
     * This case only possible for not eager singletons that is injected by UI-scoped components.
     */
    private void subscribeToViewEventBus(T instance) {
        Class<?> instanceClass = instance.getClass();
        if (!ReflectionUtils.getMethods(MethodResolutionPredicates.ViewEventHandlers, instanceClass).isEmpty()) {
            if (!Scopes.isSingleton(injector.getBinding(instanceClass))) {
                EventBus viewEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.ViewEventBus.class));
                viewEventBus.subscribe(instance);
                logger.info(String.format("[%s] subscribed to ViewEventBus [#%d]", instance.toString(), viewEventBus.hashCode()));
            } else {
                throw new ProvisionException("ERROR: Do not allowed to register Singleton-scoped instances as UI-scoped ViewEventBus subscriber");
            }
        }
    }

    /**
     * @see EventPublisherInjector#subscribeToViewEventBus(Object)
     */
    private void subscribeToModelEventBus(T instance) {
        Class<?> instanceClass = instance.getClass();
        if (!ReflectionUtils.getMethods(MethodResolutionPredicates.ModelEventHandlers, instanceClass).isEmpty()) {
            if (!Scopes.isSingleton(injector.getBinding(instanceClass))) {
                EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.ModelEventBus.class));
                modelEventBus.subscribe(instance);
                logger.info(String.format("[%s] subscribed to ModelEventBus [#%d]", instance.toString(), modelEventBus.hashCode()));
            } else {
                throw new ProvisionException("ERROR: Do not allowed to register Singleton-scoped instances as UI-scoped ModelEventBus subscriber");
            }
        }
    }

    private void subscribeToSharedModelEventBus(T instance) {
        Class<?> instanceClass = instance.getClass();
        if (!ReflectionUtils.getMethods(MethodResolutionPredicates.SharedModelEventHandlers, instanceClass).isEmpty()) {
            EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.SharedModelEventBus.class));
            modelEventBus.subscribe(instance);
            logger.info(String.format("[%s] subscribed to SharedModelEventBus [#%d]", instance, modelEventBus.hashCode()));
        }
    }
}