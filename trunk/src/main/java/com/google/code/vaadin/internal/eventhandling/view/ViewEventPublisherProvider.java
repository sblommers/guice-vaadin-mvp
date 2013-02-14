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

import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.EventBuses;
import com.google.code.vaadin.mvp.eventhandling.ViewEventPublisher;
import org.slf4j.Logger;

import javax.inject.Inject;
import javax.inject.Provider;

/**
 * ViewEventPublisherProvider - TODO: description
 *
 * @author Alexey Krylov
 * @since 26.01.13
 */
class ViewEventPublisherProvider implements Provider<ViewEventPublisher> {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Logger logger;
    private EventBus viewEventBus;

    /*===========================================[ INTERFACE METHODS ]============*/

    @Inject
    public void init(Logger logger, @EventBuses.ViewEventBus EventBus viewEventBus) {
        this.logger = logger;
        this.viewEventBus = viewEventBus;
    }

    @Override
    public ViewEventPublisher get() {
        ViewEventPublisher viewEventPublisher = new ViewEventPublisher() {
            @Override
            public void publish(Object event) {
                viewEventBus.publish(event);
            }
        };

        logger.debug(String.format("ViewEventPublisher created: [%d]", viewEventBus.hashCode()));
        return viewEventPublisher;
    }
}
