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

package com.google.code.vaadin.mvp.eventhandling;

import com.google.code.vaadin.application.AbstractMVPApplicationModule;
import com.google.code.vaadin.components.eventhandling.configuration.EventBusBinder;

/**
 * Represents all framework-provided types of Event Buses.
 *
 * @author Alexey Krylov
 * @see AbstractMVPApplicationModule#bindEventBuses(EventBusBinder)
 * @since 22.02.13
 */
public enum EventBusTypes {
    VIEW(EventType.VIEW),
    MODEL(EventType.MODEL),
    SHARED_MODEL(EventType.SHARED_MODEL);

    private EventType eventType;

    EventBusTypes(EventType eventType) {
        this.eventType = eventType;
    }

    public EventType getEventType() {
        return eventType;
    }
}