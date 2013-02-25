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

package com.google.code.vaadin.internal.eventhandling;

import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.inject.Provider;
import net.engio.mbassy.IMessageBus;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * AbstractEventBusProvider - TODO: description
 *
 * @author Alexey Krylov
 * @since 26.01.13
 */
public abstract class AbstractEventBusProvider implements Provider<EventBus> {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public EventBus get() {
        Logger logger = LoggerFactory.getLogger(getClass());
        EventBus eventBus = createEventBus();
        logger.debug("EventBus created: #" + eventBus.hashCode());
        return eventBus;
    }

    protected EventBus createEventBus() {
        return new DefaultEventBus(getMessageBus());
    }

    public abstract IMessageBus getMessageBus();
}