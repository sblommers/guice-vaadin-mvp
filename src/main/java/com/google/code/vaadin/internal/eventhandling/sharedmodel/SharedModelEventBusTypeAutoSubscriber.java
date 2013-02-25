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

import com.google.code.vaadin.internal.eventhandling.EventBusSubscriber;
import com.google.code.vaadin.internal.eventhandling.EventBusTypeAutoSubscriber;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;


/**
 * SharedModelEventBusSubscriber - TODO: description
 *
 * @author Alexey Krylov
 * @since 14.02.13
 */
class SharedModelEventBusTypeAutoSubscriber extends EventBusTypeAutoSubscriber {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private AccessibleSharedEventBusSubscribersRegistry registry;

    /*===========================================[ CONSTRUCTORS ]=================*/


    SharedModelEventBusTypeAutoSubscriber(EventBusType eventBusType, AccessibleSharedEventBusSubscribersRegistry registry) {
        super(eventBusType);
        this.registry = registry;
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected EventBusSubscriber<Object> createEventBusSubscriber() {
        return new EventBusSubscriber<Object>(injector, eventBusType) {
            @Override
            protected void postSubscribe(Object instance) {
                registry.registerSubscriber(instance);
            }
        };
    }
}
