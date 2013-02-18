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

package com.google.code.vaadin.junit.mvp.eventhandling;

import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import com.google.code.vaadin.mvp.eventhandling.events.SharedModelEvent;

import javax.inject.Inject;

/**
 * @author Alexey Krylov
 * @since 14.02.13
 */
public class SharedEventBusReceiverService {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private int sharedModelEventReceivedCount;
    private NonScopedServiceComponent component;

	/*===========================================[ CLASS METHODS ]================*/

    @Inject
    private void init(@EventBuses.SharedModelEventBus EventBus eventBus, NonScopedServiceComponent component) {
        eventBus.subscribe(this);
        this.component = component;
        eventBus.subscribe(component);
    }

    @Observes(EventType.SHARED_MODEL)
    public void sharedModelEventReceived(SharedModelEvent sharedModelEvent) {
        sharedModelEventReceivedCount++;
    }

	/*===========================================[ GETTER/SETTER ]================*/

    public int getSharedModelEventReceivedCount() {
        return sharedModelEventReceivedCount + component.getSharedModelEventReceivedCount();
    }
}
