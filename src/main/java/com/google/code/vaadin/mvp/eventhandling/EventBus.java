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

import javax.validation.constraints.NotNull;

/**
 * Dispatches events to listeners, and provides ways for listeners to register
 * themselves.
 * <p/>
 * The EventBus allows publish-subscribe-style communication between
 * components without requiring the components to explicitly register with one
 * another (and thus be aware of each other).
 *
 * @author Alexey Krylov
 * @see Observes
 * @see EventType
 * @see EventBusTypes
 * @since 26.01.13
 */
public interface EventBus extends EventPublisher {

    /*===========================================[ INTERFACE METHODS ]==============*/

    /**
     * Registers all handler methods on {@code subscriber} to receive events.
     *
     * @param subscriber object whose handler methods should be registered
     *
     * @throws IllegalArgumentException if specified {@code subscriber} is null
     */
    void subscribe(@NotNull Object subscriber);

    /**
     * Unregisters all handler methods on a registered {@code object}.
     *
     * @param subscriber object whose handler methods should be unregistered.
     *
     * @return {@code true} if unsubscribing successful
     *
     * @throws IllegalArgumentException if specified {@code subscriber} is null
     */
    boolean unsubscribe(@NotNull Object subscriber);
}
