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

import java.lang.annotation.*;

/**
 * Marks any method of any object as an event handler.
 *
 * @author Alexey Krylov
 * @see EventBusType
 * @see EventBus
 * @since 23.01.13
 */
@Target(ElementType.METHOD)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
public @interface Observes {

    /**
     * Expected Event type. Directly linked with {@link EventBusType}.
     *
     * @return type of Event-source bus.
     */
    EventType value() default EventType.VIEW;

    /**
     * Marked method will not be invoked if {@code enabled} is false.
     *
     * @return enabled/disabled flag
     */
    boolean enabled() default true;
}
