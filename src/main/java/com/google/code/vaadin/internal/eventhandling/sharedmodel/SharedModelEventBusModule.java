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

package com.google.code.vaadin.internal.eventhandling.sharedmodel;

import com.google.code.vaadin.internal.eventhandling.AbstractEventBusModule;
import com.google.code.vaadin.internal.eventhandling.EventBusTypeAutoSubscriber;
import com.google.code.vaadin.components.eventhandling.configuration.EventBusBinding;
import com.google.code.vaadin.mvp.eventhandling.EventBus;
import com.google.code.vaadin.mvp.eventhandling.SharedModelEventPublisher;
import com.google.inject.Provider;
import com.google.inject.Scope;
import com.google.inject.Scopes;
import net.engio.mbassy.bus.IMessageBus;

/**
 * @author Alexey Krylov
 * @since 14.02.13
 */
public class SharedModelEventBusModule extends AbstractEventBusModule {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private AccessibleSharedEventBusSubscribersRegistry registry;

    /*===========================================[ CONSTRUCTORS ]=================*/

    /*===========================================[ CONSTRUCTORS ]=================*/

    public SharedModelEventBusModule(EventBusBinding eventBusBinding) {
        super(eventBusBinding);
    }

    /*===========================================[ CLASS METHODS ]================*/

    @Override
    protected void configure() {
        AccessibleSharedEventBusSubscribersRegistry registry = new AccessibleSharedEventBusSubscribersRegistry();
        this.registry = registry;
        bind(SharedEventBusSubscribersRegistry.class).to(AccessibleSharedEventBusSubscribersRegistry.class);
        bind(AccessibleSharedEventBusSubscribersRegistry.class).toInstance(registry);
        super.configure();
    }

    @Override
    protected EventBusTypeAutoSubscriber createEventBusTypeAutoSubscriber() {
        return new SharedModelEventBusTypeAutoSubscriber(eventBusBinding.getType(), registry);
    }

    /*===========================================[ INTERFACE METHODS ]============*/

    @Override
    protected Class<? extends Provider<? extends IMessageBus>> getMessageBusProviderClass() {
        return SharedModelMessageBusProvider.class;
    }

    @Override
    protected Class<? extends Provider<? extends EventBus>> getEventBusProviderClass() {
        return SharedModelEventBusProvider.class;
    }

    @Override
    protected void bindSpecificEventPublisher() {
        bind(SharedModelEventPublisher.class).toProvider(SharedModelEventPublisherProvider.class).in(getBindScope());
    }

    @Override
    protected Scope getBindScope() {
        return Scopes.SINGLETON;
    }
}