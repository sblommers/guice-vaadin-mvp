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

import com.google.common.base.Preconditions;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

/**
 * @author Alexey Krylov (lexx)
 * @since 20.02.13
 */
class DefaultEventBusBinder implements EventBusBinder {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Collection<EventBusBinding> bindings;

    /*===========================================[ CONSTRUCTORS ]=================*/

    DefaultEventBusBinder() {
        bindings = new ArrayList<>();
    }

    @Override
    public EventBusBindingBuilder bind(@NotNull EventBusTypes type) {
        Preconditions.checkArgument(type != null, "Specified EventBusType is null or empty");

        return new DefaultEventBusBindingBuilder(type) {
            @Override
            protected EventBusBinding build() {
                EventBusBinding binding = super.build();
                bindings.add(binding);
                return binding;
            }
        };
    }

    @Override
    public Collection<EventBusBinding> getBindings() {
        return Collections.unmodifiableCollection(bindings);
    }
}
