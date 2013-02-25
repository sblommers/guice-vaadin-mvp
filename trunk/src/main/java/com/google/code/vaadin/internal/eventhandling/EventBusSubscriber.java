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
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.inject.MembersInjector;
import net.engio.mbassy.common.ReflectionUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Подписывает инжектируемый компонент на шины сообщений, если тот имеет методы с аннотацией {@link
 * Observes}.
 *
 * @author Alexey Krylov
 * @see MethodResolutionPredicates
 * @since 14.02.13
 */
public class EventBusSubscriber<T> implements MembersInjector<T> {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(EventBusSubscriber.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBus eventBus;
    private EventType eventType;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public EventBusSubscriber(EventBus eventBus, EventType eventType) {
        this.eventBus = eventBus;
        this.eventType = eventType;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void injectMembers(T instance) {
        Class<?> instanceClass = instance.getClass();
        if (isMessageListener(instanceClass)) {
            eventBus.subscribe(instance);
            postSubscribe(instance);
            logger.info(String.format("[%s] subscribed to EventBus [#%d]", instance.toString(), eventBus.hashCode()));
        }
    }

    protected boolean isMessageListener(Class<?> instanceClass) {
        return !ReflectionUtils.getMethods(MethodResolutionPredicates.getEventHandlersPredicate(eventType), instanceClass).isEmpty();
    }

    protected void postSubscribe(T instance) {
    }
}