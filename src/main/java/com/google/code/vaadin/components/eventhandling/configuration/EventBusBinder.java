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

package com.google.code.vaadin.components.eventhandling.configuration;

import com.google.code.vaadin.application.AbstractMVPApplicationModule;
import com.google.code.vaadin.mvp.eventhandling.EventBusTypes;

import javax.validation.constraints.NotNull;

/**
 * Collects all Event Buses binding configurations.
 * <p/>
 * Example:
 * <pre>
 * bind(EventBusTypes.VIEW).withDefaultConfiguration();
 * </pre>
 *
 * @author Alexey Krylov
 * @see AbstractMVPApplicationModule#bindEventBuses(EventBusBinder)
 * @since 20.02.13
 */
public interface EventBusBinder {

    /**
     * Initiates binding process for specified EventBus type.
     *
     * @param type bus type
     *
     * @return binding builder instance
     *
     * @throws IllegalArgumentException if specified {@code type} is null
     */
    EventBusBindingBuilder bind(@NotNull EventBusTypes type);

    EventBusBinding getBinding(@NotNull EventBusTypes type);
}
