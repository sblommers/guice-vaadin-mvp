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

import com.google.code.vaadin.internal.eventhandling.AbstractEventBusModule;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.internal.eventhandling.configuration.EventBusTypes;
import com.google.code.vaadin.mvp.eventhandling.EventBusType;
import com.google.code.vaadin.mvp.eventhandling.EventPublisher;
import com.google.code.vaadin.mvp.eventhandling.ViewEventPublisher;
import com.google.inject.Provider;
import net.engio.mbassy.BusConfiguration;

import javax.inject.Inject;
import javax.validation.constraints.NotNull;

/**
 * ViewEventBusModule - TODO: description
 *
 * @author Alexey Krylov (lexx)
 * @since 24.02.13
 */
public class ViewEventBusModule extends AbstractEventBusModule {

	/*===========================================[ INSTANCE VARIABLES ]===========*/

    private EventBusBinding eventBusBinding;

	/*===========================================[ CONSTRUCTORS ]=================*/

    public ViewEventBusModule(EventBusBinding eventBusBinding) {
        this.eventBusBinding = eventBusBinding;
    }

    @Override
    protected void configure() {
        bindViewEventBus();
    }

    protected void bindViewEventBus() {
        BusConfiguration configuration = mapObservesAnnotations(EventBusTypes.VIEW,
                eventBusBinding != null ? eventBusBinding.getConfiguration() : createDefaultViewEventBusConfiguration());
        bindEventBus(EventBusTypes.VIEW, configuration);

        bind(ViewEventPublisher.class).toProvider(new Provider<ViewEventPublisher>() {
            @Inject
            @EventBusType(EventBusTypes.VIEW)
            private EventPublisher publisher;

            @Override
            public ViewEventPublisher get() {
                return new ViewEventPublisher() {
                    @Override
                    public void publish(@NotNull Object event) {
                        publisher.publish(event);
                    }
                };
            }
        });
    }

    protected BusConfiguration createDefaultViewEventBusConfiguration() {
        return BusConfiguration.Default();
    }
}