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

import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import net.engio.mbassy.common.IPredicate;
import net.engio.mbassy.listener.Handler;

import java.lang.reflect.Method;

/**
 * MBassador extension of Event Handler resolution - it scans methods marked with {@link Observes} annotation only.
 * Default {@link Handler} is not supported because it's too powerful for Guice-Vaadin-MVP internals and can bring
 * complexity in event publishing/handling (for example - because there is at least three available Event Buses - view,
 * model and shared model).
 *
 * @author Alexey Krylov
 * @since 13.02.13
 */
public class MethodResolutionPredicates {

	/*===========================================[ CONSTRUCTORS ]=================*/

    private MethodResolutionPredicates() {
    }

	/*===========================================[ CLASS METHODS ]================*/

    public static IPredicate<Method> getEventHandlersPredicate(final EventType eventType) {
        return new IPredicate<Method>() {
            @Override
            public boolean apply(Method target) {
                Observes observes = target.getAnnotation(Observes.class);
                return observes != null && observes.value().equals(eventType);
            }
        };
    }
}