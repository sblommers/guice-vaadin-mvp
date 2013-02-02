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

package com.google.code.vaadin.internal.event;

import com.google.code.vaadin.mvp.EventBus;
import com.google.common.base.Preconditions;
import net.engio.mbassy.IMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.validation.constraints.NotNull;

/**
 * DefaultAccessibleEventBus - TODO: description
 *
 * @author Alexey Krylov (AleX)
 * @since 26.01.13
 */
class DefaultEventBus implements EventBus {

    /*===========================================[ STATIC VARIABLES ]=============*/

    private static final Logger logger = LoggerFactory.getLogger(DefaultEventBus.class);

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private IMessageBus bus;

    /*===========================================[ CONSTRUCTORS ]=================*/

    DefaultEventBus(IMessageBus bus) {
        this.bus = bus;
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public void subscribe(@NotNull Object subscriber) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Subscribing: [%s] -> [#%d]", subscriber, hashCode()));
        }
        bus.subscribe(subscriber);
    }

    @Override
    public void unsubscribe(@NotNull Object subscriber) {
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Unsubscribing: [%s] -> [#%d]", subscriber, hashCode()));
        }
        bus.unsubscribe(subscriber);
    }

    @Override
    public void publish(@NotNull Object event) {
        Preconditions.checkArgument(event != null, "Published Event can't be null");
        if (logger.isDebugEnabled()) {
            logger.debug(String.format("Event: [%s] -> [#%d]", event, hashCode()));
        }
        bus.post(event).now();
    }
}
