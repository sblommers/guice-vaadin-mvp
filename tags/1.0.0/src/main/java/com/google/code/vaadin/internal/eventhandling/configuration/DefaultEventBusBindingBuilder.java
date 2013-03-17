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

import com.google.code.vaadin.components.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.components.eventhandling.configuration.EventBusBindingBuilder;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import net.engio.mbassy.bus.BusConfiguration;

/**
 * @author Alexey Krylov
 * @since 20.02.13
 */
class DefaultEventBusBindingBuilder implements EventBusBindingBuilder {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusType type;
    private BusConfiguration configuration;

	/*===========================================[ CONSTRUCTORS ]=================*/

    DefaultEventBusBindingBuilder(EventBusType type) {
        this.type = type;
    }

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void withConfiguration(BusConfiguration configuration) {
        this.configuration = configuration;
        build();
    }

    @Override
    public void withDefaultConfiguration() {
        withConfiguration(BusConfiguration.Default());
    }

    protected EventBusBinding build() {
        AccessibleEventBusBinding binding = new AccessibleEventBusBinding();
        binding.setType(type);
        binding.setConfiguration(configuration);
        return binding;
    }
}
