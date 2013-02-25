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

package com.google.code.vaadin.internal.eventhandling.model;

import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import com.google.code.vaadin.mvp.eventhandling.ModelEventPublisher;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * ModelEventPublisherProvider - TODO: description
 *
 * @author Alexey Krylov
 * @since 26.01.13
 */
class ModelEventPublisherProvider implements Provider<ModelEventPublisher> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    @EventBusType(EventBusTypes.MODEL)
    private EventBus eventBus;

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public ModelEventPublisher get() {
        return new ModelEventPublisher() {
            @Override
            public void publish(Object event) {
                eventBus.publish(event);
            }
        };
    }
}
