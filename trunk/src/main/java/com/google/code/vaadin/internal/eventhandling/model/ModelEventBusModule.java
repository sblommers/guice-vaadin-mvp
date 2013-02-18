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

package com.google.code.vaadin.internal.eventhandling.model;

import com.google.code.vaadin.application.uiscope.UIScope;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.code.vaadin.mvp.eventhandling.ModelEventPublisher;
import com.google.inject.AbstractModule;
import net.engio.mbassy.IMessageBus;

/**
 * ModelEventBusModule - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 14.02.13
 */
public class ModelEventBusModule extends AbstractModule {

	/*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected void configure() {
        bind(EventBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelEventBusProvider.class).in(UIScope.getCurrent());
        bind(IMessageBus.class).annotatedWith(EventBuses.ModelEventBus.class).toProvider(ModelMessageBusProvider.class).in(UIScope.getCurrent());
        bind(ModelEventPublisher.class).toProvider(ModelEventPublisherProvider.class).in(UIScope.getCurrent());
    }
}
