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
import com.google.code.vaadin.mvp.eventhandling.EventType;
import com.google.code.vaadin.mvp.eventhandling.Observes;
import net.engio.mbassy.common.IPredicate;

import java.lang.reflect.Method;

/**
 * MethodResolutionPredicates - TODO: description
 *
 * @author Alexey Krylov
 * @since 13.02.13
 */
public class MethodResolutionPredicates {

    /*===========================================[ INTERFACE METHODS ]==============*/

    IPredicate<Method> AllEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            return target.getAnnotation(Observes.class) != null;
        }
    };

    IPredicate<Method> ViewEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            Observes observes = target.getAnnotation(Observes.class);
            return observes != null && observes.value().equals(EventType.VIEW);
        }
    };

    IPredicate<Method> ModelEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            Observes observes = target.getAnnotation(Observes.class);
            return observes != null && observes.value().equals(EventType.MODEL);
        }
    };

    IPredicate<Method> SharedModelEventHandlers = new IPredicate<Method>() {
        @Override
        public boolean apply(Method target) {
            Observes observes = target.getAnnotation(Observes.class);
            return observes != null && observes.value().equals(EventType.SHARED_MODEL);
        }
    };


    public static IPredicate<Method> getEventHandlersPredicate(EventBusTypes eventBusType){

    }
}
