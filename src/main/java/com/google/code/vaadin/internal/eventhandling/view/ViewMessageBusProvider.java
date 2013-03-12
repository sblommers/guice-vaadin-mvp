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

package com.google.code.vaadin.internal.eventhandling.view;

import com.google.code.vaadin.internal.eventhandling.AbstractMessageBusProvider;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import com.google.inject.Inject;
import net.engio.mbassy.bus.BusConfiguration;

/**
 * ViewMessageBusProvider - TODO: description
 *
 * @author Alexey Krylov
 * @since 26.01.13
 */
class ViewMessageBusProvider extends AbstractMessageBusProvider {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    @Inject
    @EventBusType(EventBusTypes.VIEW)
    private BusConfiguration busConfiguration;

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected BusConfiguration getConfiguration() {
        return busConfiguration;
    }
}