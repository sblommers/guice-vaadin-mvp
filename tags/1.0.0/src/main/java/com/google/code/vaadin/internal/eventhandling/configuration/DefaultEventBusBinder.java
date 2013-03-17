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

package com.google.code.vaadin.internal.eventhandling.configuration;

import com.google.code.vaadin.components.eventhandling.configuration.EventBusBinder;
import com.google.code.vaadin.components.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.components.eventhandling.configuration.EventBusBindingBuilder;
import com.google.code.vaadin.internal.eventhandling.AbstractEventBusModule;
import com.google.code.vaadin.mvp.eventhandling.EventBusTypes;
import com.google.common.base.Preconditions;

import javax.validation.constraints.NotNull;
import java.util.EnumMap;
import java.util.Map;

/**
 * @author Alexey Krylov
 * @since 20.02.13
 */
public class DefaultEventBusBinder implements EventBusBinder {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Map<EventBusTypes, EventBusBinding> bindings;

    /*===========================================[ CONSTRUCTORS ]=================*/

    public DefaultEventBusBinder() {
        bindings = new EnumMap<>(EventBusTypes.class);
    }

    @Override
    public EventBusBindingBuilder bind(@NotNull EventBusTypes type) {
        Preconditions.checkArgument(type != null, "Specified EventBusType is null");

        return new DefaultEventBusBindingBuilder(AbstractEventBusModule.eventBusType(type)) {
            @Override
            protected EventBusBinding build() {
                EventBusBinding binding = super.build();
                bindings.put(binding.getType().value(), binding);
                return binding;
            }
        };
    }

    @Override
    public EventBusBinding getBinding(@NotNull EventBusTypes type) {
        Preconditions.checkArgument(type != null, "Specified EventBusType is null");

        return bindings.get(type);
    }
}
