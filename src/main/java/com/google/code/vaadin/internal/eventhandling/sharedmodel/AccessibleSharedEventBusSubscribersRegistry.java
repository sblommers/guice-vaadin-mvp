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

import com.google.code.vaadin.application.ui.ScopedUI;
import com.google.code.vaadin.application.uiscope.UIKey;
import com.google.common.base.Preconditions;
import com.vaadin.ui.UI;

import javax.validation.constraints.NotNull;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * todo
 * @author Alexey Krylov
 * @since 27.01.13
 */
class AccessibleSharedEventBusSubscribersRegistry implements SharedEventBusSubscribersRegistry {

    /*===========================================[ INSTANCE VARIABLES ]===========*/

    private Map<UIKey, Collection<Object>> subscribersMap =  new ConcurrentHashMap<>();

    /*===========================================[ CLASS METHODS ]================*/

    void registerSubscriber(Object subscriber) {
        UIKey key = ((ScopedUI) UI.getCurrent()).getInstanceKey();

        Collection subscribers = subscribersMap.get(key);
        if (subscribers == null) {
            subscribers = Collections.synchronizedList(new ArrayList());
            subscribersMap.put(key, subscribers);
        }

        subscribers.add(subscriber);
    }

    @Override
    public Collection<Object> removeAndGetSubscribers(@NotNull UIKey uiKey) {
        Preconditions.checkArgument(uiKey != null, "UIKey can't be null");

        Collection<Object> removed = subscribersMap.remove(uiKey);
        if (removed == null){
            removed = new ArrayList<>();
        }
        return removed;
    }
}