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

import com.google.code.vaadin.mvp.EventBus;
import com.google.code.vaadin.mvp.EventBuses;
import com.google.inject.Injector;
import com.google.inject.Key;
import com.google.inject.MembersInjector;
import com.google.inject.Scopes;
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

    private Injector injector;

	/*===========================================[ CONSTRUCTORS ]=================*/

    EventPublisherInjector(Injector injector) {
        this.injector = injector;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        Class<?> instanceClass = instance.getClass();
        // todo simplify & remove explicit subscription to UIScoped model eventbus
        // todo direct subscription: Presenter -> ViewEventBus (UIScoped). All magic should be removed

        // Subscribe only if @Observes/@Listener methods present
        if (!ReflectionUtils.getMethods(CompositeMetadataReader.AllMessageHandlers, instanceClass).isEmpty()) {
            /**
             * Do not allowed to register Singleton-scoped instances as UI-scoped ViewEventBus subscriber.
             * This case only possible for not eager singletons that is injected by Session-scoped components.
             */
            if (!Scopes.isSingleton(injector.getBinding(instanceClass))) {
                EventBus viewEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.ViewEventBus.class));
                EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.ModelEventBus.class));
                //todo uiscope   !!!!!!
                //injector.getInstance(DefaultEventBusSubscribersRegistry.class).registerUIScopedSubscriber(instance);
                viewEventBus.subscribe(instance);
                modelEventBus.subscribe(instance);
                logger.info(String.format("[%s] subscribed to ViewEventBus [#%d] and ModelEventBus [#%d]", instance.toString(), viewEventBus.hashCode(), modelEventBus.hashCode()));
            }

            EventBus modelEventBus = injector.getInstance(Key.get(EventBus.class, EventBuses.GlobalModelEventBus.class));
            modelEventBus.subscribe(instance);
            logger.info(String.format("[%s] subscribed to GlobalModelEventBus [#%d]", instance, modelEventBus.hashCode()));
        }
    }
}