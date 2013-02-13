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

import net.engio.mbassy.BusConfiguration;
import net.engio.mbassy.IMessageBus;
import net.engio.mbassy.MBassador;

import javax.inject.Provider;


/**
 * AbstractEventBusProvider - TODO: description
 *
 * @author Alexey Krylov
 * @since 26.01.13
 */
public abstract class AbstractMessageBusProvider implements Provider<IMessageBus> {

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    public IMessageBus get() {
        BusConfiguration busConfiguration = getConfiguration();
        if (busConfiguration == null) {
            busConfiguration = BusConfiguration.Default();
        }

        busConfiguration = mapAnnotations(busConfiguration);
        return new MBassador(busConfiguration);
    }

    protected abstract BusConfiguration getConfiguration();

    protected BusConfiguration mapAnnotations(BusConfiguration busConfiguration) {
        busConfiguration.setMetadataReader(new CompositeMetadataReader());
        return busConfiguration;
    }
}
