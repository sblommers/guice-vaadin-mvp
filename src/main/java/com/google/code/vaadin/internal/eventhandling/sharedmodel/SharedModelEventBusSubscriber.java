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

import com.google.code.vaadin.internal.eventhandling.MethodResolutionPredicates;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import net.engio.mbassy.common.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * SharedModelEventBusSubscriber - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class SharedModelEventBusSubscriber<T> implements MembersInjector<T> {

	/*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(SharedModelEventBusSubscriber.class);

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private Injector injector;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public SharedModelEventBusSubscriber(Injector injector) {
        this.injector = injector;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        Class<?> instanceClass = instance.getClass();
        if (!ReflectionUtils.getMethods(MethodResolutionPredicates.SharedModelEventHandlers, instanceClass).isEmpty()) {
            EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.SharedModelEventBus.class));
            modelEventBus.subscribe(instance);

            // Register instance as SharedEventBus subscriber to unsubscribe later on UI.detach
            injector.getInstance(AccessibleSharedEventBusSubscribersRegistry.class).registerSubscriber(instance);

            logger.info(String.format("[%s] subscribed to SharedModelEventBus [#%d]", instance, modelEventBus.hashCode()));
        }
    }
}
