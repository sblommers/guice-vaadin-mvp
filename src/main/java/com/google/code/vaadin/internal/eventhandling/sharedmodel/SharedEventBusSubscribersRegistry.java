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

import com.google.code.vaadin.application.ui.ScopedUIProvider;
import com.google.code.vaadin.application.uiscope.UIKey;

import javax.validation.constraints.NotNull;
import java.util.Collection;

/**
 * Registry of Shared Model Event Bus subscribers. It's required to automatically unsubscribe UI-scoped subscribers
 * when UI detaches.
 *
 * @author Alexey Krylov
 * @see ScopedUIProvider#createScopedUIDetachListener(Class, UIKey)
 * @since 28.01.13
 */
public interface SharedEventBusSubscribersRegistry {

    /*===========================================[ INTERFACE METHODS ]==============*/

    Collection<Object> removeAndGetSubscribers(@NotNull UIKey uiKey);

    Collection<Object> getSubscribers(@NotNull UIKey uiKey);
}