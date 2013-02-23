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

import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;

import java.lang.annotation.Annotation;

/**
 * ObservesImpl - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.02.13
 */
class EventBusTypeImpl implements EventBusType {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusTypes value;

	/*===========================================[ CONSTRUCTORS ]=================*/

    EventBusTypeImpl(EventBusTypes value) {
        this.value = value;
    }

	/*===========================================[ CLASS METHODS ]================*/

    public boolean equals(Object o) {
        if (!(o instanceof EventBusType)) {
            return false;
        }

        EventBusType other = (EventBusType) o;
        return value.equals(other.value());
    }

    public int hashCode() {
        return (127 * "value".hashCode()) ^ value.hashCode();
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public EventBusTypes value() {
        return value;
    }

    public String toString() {
        return "@" + EventBusType.class.getName() + "(value=" + value + ")";
    }

    @Override
    public Class<? extends Annotation> annotationType() {
        return EventBusType.class;
    }
}